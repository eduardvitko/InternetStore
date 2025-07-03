package com.example.InternetStore.services;

import com.example.InternetStore.dto.OrderDto;
import com.example.InternetStore.dto.OrderItemDto;
import com.example.InternetStore.model.*;
import com.example.InternetStore.reposietories.OrderRepository;
import com.example.InternetStore.reposietories.ProductRepository;
import com.example.InternetStore.reposietories.UserRepository;
import com.example.InternetStore.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderItemService orderItemService;
    private final ProductRepository productRepository;

    public OrderDto mapToDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus().name())
                .total(order.getTotal())
                .items(order.getItems().stream()
                        .map(orderItemService::mapToDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public Order mapToEntity(OrderDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setId(dto.getId());
        order.setUser(user);
        order.setOrderDate(dto.getOrderDate());
        order.setStatus(Enum.valueOf(com.example.InternetStore.model.OrderStatus.class, dto.getStatus()));
        order.setTotal(dto.getTotal());

        List<OrderItem> items = dto.getItems().stream()
                .map(orderItemService::mapToEntity)
                .peek(item -> item.setOrder(order))
                .collect(Collectors.toList());

        order.setItems(items);
        return order;
    }

    public OrderDto saveOrder(OrderDto dto) {
        Order saved = orderRepository.save(mapToEntity(dto));
        return mapToDto(saved);
    }

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<OrderDto> getOrdersByUserId(Integer userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public void deleteOrder(Integer id) {
        orderRepository.deleteById(id);
    }

    @Transactional
    public OrderDto updateOrder(Integer id, OrderDto orderDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id " + id));

        // Оновлюємо статус та total
        order.setStatus(OrderStatus.valueOf(orderDto.getStatus()));
        order.setTotal(orderDto.getTotal());

        // Очищаємо старі позиції
        order.getItems().clear();

        // Додаємо нові позиції з DTO
        List<OrderItem> items = orderDto.getItems().stream()
                .map(dto -> {
                    Product product = productRepository.findById(dto.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found with id " + dto.getProductId()));
                    OrderItem item = new OrderItem();
                    item.setOrder(order);
                    item.setProduct(product);
                    item.setQuantity(dto.getQuantity());
                    item.setPrice(dto.getPrice());
                    return item;
                }).toList();

        order.getItems().addAll(items);

        Order savedOrder = orderRepository.save(order);

        // Ручне конвертування з Order в OrderDto для відповіді
        List<OrderItemDto> itemDtos = savedOrder.getItems().stream()
                .map(item -> new OrderItemDto(
                        item.getId(),
                        item.getProduct().getId(),
                        item.getQuantity(),
                        item.getPrice()
                ))
                .toList();

        return new OrderDto(
                savedOrder.getId(),
                savedOrder.getUser().getId(),
                savedOrder.getOrderDate(),
                savedOrder.getStatus().name(),
                savedOrder.getTotal(),
                itemDtos
        );
    }

    public User getCurrentUser() {
        CustomUserDetails userDetails = (CustomUserDetails)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public OrderDto createOrderFromItems(User user, List<OrderItemDto> itemDtos) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(java.time.LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> items = itemDtos.stream().map(dto -> {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getStock() < dto.getQuantity()) {
                throw new RuntimeException("Недостатньо товару: " + product.getName());
            }

            // Зменшуємо залишок
            product.setStock(product.getStock() - dto.getQuantity());
            productRepository.save(product);

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(dto.getQuantity());
            item.setPrice(dto.getPrice());

            return item;
        }).toList();

        order.setItems(items);
        order.setTotal(items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        Order saved = orderRepository.save(order);

        // Мапінг вручну
        List<OrderItemDto> resultItems = saved.getItems().stream()
                .map(i -> new OrderItemDto(
                        i.getId(),
                        i.getProduct().getId(),
                        i.getQuantity(),
                        i.getPrice()
                )).toList();

        return new OrderDto(
                saved.getId(),
                saved.getUser().getId(),
                saved.getOrderDate(),
                saved.getStatus().name(),
                saved.getTotal(),
                resultItems
        );
    }

    // Відміняємо замовлення
    @Transactional
    public void cancelOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Замовлення не знайдено"));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Замовлення вже скасоване");
        }
        // Додана перевірка, щоб не скасовувати вже оплачене замовлення
        if (order.getStatus() == OrderStatus.PAID) {
            throw new RuntimeException("Неможливо скасувати оплачене або завершене замовлення.");
        }


        // Повертаємо товари на склад
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }

        // Ставимо статус "CANCELLED"
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

//    ---
//
//            ## Новий метод: Зміна статусу замовлення на PAID
//
//    ```java
    @Transactional
    public void markOrderAsPaid(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Замовлення не знайдено з ID: " + orderId));

        // Перевіряємо поточний статус, щоб переконатися, що можна перевести в PAID
        if (order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(OrderStatus.PAID);
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Неможливо перевести замовлення ID " + orderId +
                    " в статус PAID. Поточний статус: " + order.getStatus());
        }
    }
}