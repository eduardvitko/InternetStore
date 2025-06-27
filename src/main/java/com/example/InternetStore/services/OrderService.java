package com.example.InternetStore.services;



import com.example.InternetStore.dto.OrderDto;
import com.example.InternetStore.dto.OrderItemDto;
import com.example.InternetStore.model.Order;
import com.example.InternetStore.model.OrderItem;
import com.example.InternetStore.model.OrderStatus;
import com.example.InternetStore.model.User;
import com.example.InternetStore.reposietories.OrderRepository;
import com.example.InternetStore.reposietories.ProductRepository;
import com.example.InternetStore.reposietories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                    OrderItem item = new OrderItem();
                    item.setOrder(order);
                    item.setProduct(productRepository.findById(dto.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found with id " + dto.getProductId())));
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


}
