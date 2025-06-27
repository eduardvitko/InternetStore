package com.example.InternetStore.controller;

import com.example.InternetStore.dto.OrderDto;
import com.example.InternetStore.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

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
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        OrderDto createdOrder = orderService.saveOrder(orderDto);
        return ResponseEntity.ok(createdOrder);
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
