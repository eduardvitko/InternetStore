package com.example.InternetStore.services;

import com.example.InternetStore.dto.*;
import com.example.InternetStore.model.*;
import com.example.InternetStore.reposietories.AddressRepository;
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
    private final AddressRepository addressRepository;

    public OrderDto mapToDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .username(order.getUser().getUsername())   // 👈 додано
                .phone(order.getUser().getPhone())         // 👈 додано
                .orderDate(order.getOrderDate())
                .status(order.getStatus().name())
                .total(order.getTotal())
                .address(toAddressDto(order.getAddress())) // 👈 додано
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
        return getAllOrdersWithUserAndAddress(); // вже готовий метод
    }

    public List<OrderDto> getOrdersByUserId(Integer userId) {
        return orderRepository.findAllWithUserAndAddressByUserId().stream()
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
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getPrice()
                ))
                .toList();

        return new OrderDto(
                savedOrder.getId(),
                savedOrder.getUser().getId(),
                savedOrder.getUser().getUsername(),
                savedOrder.getUser().getPhone(),
                savedOrder.getOrderDate(),
                savedOrder.getStatus().name(),
                savedOrder.getTotal(),
                null, // або передай AddressDto, якщо треба
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
                    .orElseThrow(() -> new RuntimeException("Product not found with id " + dto.getProductId()));

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
            // Використовуємо ціну з продукту, а не з DTO (безпечніше)
            item.setPrice(product.getPrice());

            return item;
        }).toList();

        order.setItems(items);
        order.setTotal(items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        Order saved = orderRepository.save(order);

        List<OrderItemDto> resultItems = saved.getItems().stream()
                .map(i -> new OrderItemDto(
                        i.getId(),
                        i.getProduct().getId(),
                        i.getProduct().getName(),
                        i.getQuantity(),
                        i.getPrice()
                )).toList();

        return new OrderDto(
                saved.getId(),
                saved.getUser().getId(),
                saved.getUser().getUsername(),
                saved.getUser().getPhone(),
                saved.getOrderDate(),
                saved.getStatus().name(),
                saved.getTotal(),
                null, // TODO: якщо потрібна адреса, передайте тут AddressDto
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




        public List<OrderWithAddressDto> getOrdersWithAddressByUserId(Integer userId) {
            return orderRepository.findAllByUser_Id(userId)
                    .stream()
                    .map(this::toOrderWithAddressDto)
                    .collect(Collectors.toList());
        }

    private OrderWithAddressDto toOrderWithAddressDto(Order order) {
        OrderWithAddressDto dto = new OrderWithAddressDto();
        dto.setId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus().name());
        dto.setTotal(order.getTotal());
        dto.setAddress(toAddressDto(order.getAddress()));

        // 👇 додано
        if (order.getUser() != null) {
            dto.setUsername(order.getUser().getUsername());
            dto.setPhone(order.getUser().getPhone());
        }

        return dto;
    }


    private AddressDto toAddressDto(Address address) {
            if (address == null) return null;
            AddressDto dto = new AddressDto();
            dto.setId(address.getId());
            dto.setCountry(address.getCountry());
            dto.setCity(address.getCity());
            dto.setStreet(address.getStreet());
            dto.setHouseNumber(address.getHouseNumber());
            dto.setApartmentNumber(address.getApartmentNumber());
            dto.setPostalCode(address.getPostalCode());
            dto.setRegion(address.getRegion());
            dto.setIsDefault(address.getIsDefault());
            return dto;
        }
    public List<OrderDto> getAllOrdersWithUserAndAddress() {
        User currentUser = getCurrentUser(); // ← отримуємо поточного користувача
        List<Order> orders = orderRepository.findAllWithUserAndAddressByUserId();

        orders.forEach(o -> {
            System.out.println("Order " + o.getId() + " address: " + (o.getAddress() != null ? o.getAddress().toString() : "null"));
        });
        orders.forEach(o ->
                System.out.println("🟢 ID: " + o.getId() + " — дата: " + o.getOrderDate())
        );

        return orders.stream()
                .map(order -> {
                    List<OrderItemDto> itemDtos = order.getItems().stream()
                            .map(item -> OrderItemDto.builder()
                                    .id(item.getId())
                                    .productId(item.getProduct().getId())
                                    .productName(item.getProduct().getName())
                                    .quantity(item.getQuantity())
                                    .price(item.getPrice())
                                    .build())
                            .collect(Collectors.toList());

                    Address address = order.getAddress();
                    AddressDto addressDto = null;
                    if (address != null) {
                        addressDto = AddressDto.builder()
                                .country(address.getCountry())
                                .city(address.getCity())
                                .street(address.getStreet())
                                .houseNumber(address.getHouseNumber())
                                .apartmentNumber(address.getApartmentNumber())
                                .region(address.getRegion())
                                .postalCode(address.getPostalCode())
                                .build();
                    }

                    return OrderDto.builder()
                            .id(order.getId())
                            .userId(order.getUser().getId())
                            .username(order.getUser().getUsername())
                            .phone(order.getUser().getPhone())
                            .orderDate(order.getOrderDate())
                            .status(order.getStatus().name())
                            .total(order.getTotal())
                            .address(addressDto)
                            .items(itemDtos)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDto updateOrderAddress(Integer orderId, Integer addressId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        order.setAddress(address);
        orderRepository.saveAndFlush(order);

        // Вручну формуємо OrderDto
        List<OrderItemDto> itemDtos = order.getItems().stream()
                .map(item -> OrderItemDto.builder()
                        .id(item.getId())
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build())
                .collect(Collectors.toList());

        Address addressEntity = order.getAddress();
        AddressDto addressDto = null;
        if (addressEntity != null) {
            addressDto = AddressDto.builder()
                    .id(addressEntity.getId())
                    .country(addressEntity.getCountry())
                    .city(addressEntity.getCity())
                    .street(addressEntity.getStreet())
                    .houseNumber(addressEntity.getHouseNumber())
                    .apartmentNumber(addressEntity.getApartmentNumber())
                    .postalCode(addressEntity.getPostalCode())
                    .region(addressEntity.getRegion())
                    .isDefault(addressEntity.getIsDefault())
                    .build();
        }

        return OrderDto.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .username(order.getUser().getUsername())
                .phone(order.getUser().getPhone())
                .orderDate(order.getOrderDate())
                .status(order.getStatus().name())
                .total(order.getTotal())
                .address(addressDto)
                .items(itemDtos)
                .build();
    }





}
