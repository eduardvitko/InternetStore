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
    private final UserService userService;

    // Get all orders (admin or specific roles only, consider security)
    @GetMapping("/all")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // Get user's orders by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    // Create a new order
    @PostMapping("/create")
    public ResponseEntity<OrderDto> createOrder(@RequestBody List<OrderItemDto> items) {
        User user = userService.getCurrentUser()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated"));

        OrderDto dto = orderService.createOrderFromItems(user, items);
        return ResponseEntity.ok(dto);
    }

    // Delete an order
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    // Update an order (general update)
    @PutMapping("/update/{id}")
    public ResponseEntity<OrderDto> updateOrder(
            @PathVariable Integer id,
            @RequestBody OrderDto orderDto) {

        try {
            OrderDto updatedOrder = orderService.updateOrder(id, orderDto);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            // Log the error for debugging
            // logger.error("Error updating order {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build(); // Or ResponseEntity.badRequest() with a message
        }
    }

    // Endpoint to cancel an order
    @PutMapping("/{id}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Integer id) {
        orderService.cancelOrder(id);
        return ResponseEntity.ok("Замовлення скасовано, товари повернено на склад");
    }


    // --- NEW ENDPOINT: Mark order as PAID ---
    @PutMapping("/{id}/pay")
    public ResponseEntity<String> markOrderAsPaid(@PathVariable Integer id) {
        try {
            orderService.markOrderAsPaid(id); // You'll need to implement this method in OrderService
            return ResponseEntity.ok("Статус замовлення змінено на PAID.");
        } catch (RuntimeException e) {
            // Consider more specific exceptions like OrderNotFoundException
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found or could not be marked as paid.", e);
        }
    }
    // --- END OF NEW ENDPOINT ---
}