package com.example.InternetStore.controller;

import com.example.InternetStore.dto.OrderDto;
import com.example.InternetStore.dto.OrderItemDto;
import com.example.InternetStore.model.User;
import com.example.InternetStore.services.OrderService;
import com.example.InternetStore.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private  final UserService userService;

    // Отримати всі замовлення
    @GetMapping("/all")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }


    // Отримати замовлення користувача за userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    // Створити нове замовлення
    @PostMapping("/create")
    public ResponseEntity<OrderDto> createOrder(@RequestBody List<OrderItemDto> items) {
        User user = userService.getCurrentUser()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated"));

        OrderDto dto = orderService.createOrderFromItems(user, items);
        return ResponseEntity.ok(dto);
    }

    // Видалити замовлення
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
    // В контролері
    @PutMapping("/update/{id}")
    public ResponseEntity<OrderDto> updateOrder(
            @PathVariable Integer id,
            @RequestBody OrderDto orderDto) {

        try {
            OrderDto updatedOrder = orderService.updateOrder(id, orderDto);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Або ResponseEntity.badRequest() з повідомленням
        }
    }

}
