package com.example.InternetStore.controller;

import com.example.InternetStore.dto.PaymentDto;
import com.example.InternetStore.services.PaymentService;
import com.example.InternetStore.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService; // Потрібен для пошуку користувача

    // === ЕНДПОІНТИ ДЛЯ ЗВИЧАЙНИХ КОРИСТУВАЧІВ ===

    /**
     * Отримує всі платежі для поточного авторизованого користувача.
     * Доступно для будь-якого авторизованого користувача.
     * Цей ендпоінт відповідає на GET /api/payments/my
     */
    @GetMapping("/my")
    public ResponseEntity<List<PaymentDto>> getMyPayments(Authentication authentication) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }
        String username = authentication.getName();
        // Вам потрібно буде реалізувати цей метод в PaymentService
        List<PaymentDto> payments = paymentService.findPaymentsByUsername(username);
        return ResponseEntity.ok(payments);
    }

    /**
     * Створює новий платіж.
     * Доступно для будь-якого авторизованого користувача.
     * Цей ендпоінт відповідає на POST /api/payments
     */
    @PostMapping
    public ResponseEntity<PaymentDto> createPayment(@RequestBody PaymentDto paymentDto, Authentication authentication) {
        // Тут можна додати логіку перевірки, що користувач (authentication.getName())
        // є власником замовлення (paymentDto.getOrderId())
        PaymentDto createdPayment = paymentService.createPayment(paymentDto);
        return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
    }


    // === ЕНДПОІНТИ ТІЛЬКИ ДЛЯ АДМІНІСТРАТОРА ===

    /**
     * Отримує список УСІХ платежів в системі.
     * Доступно тільки для ADMIN.
     * Цей ендпоінт відповідає на GET /api/payments/all
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentDto>> getAllPayments() {
        List<PaymentDto> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    /**
     * Оновлює існуючий платіж.
     * Доступно тільки для ADMIN.
     * Цей ендпоінт відповідає на PUT /api/payments/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentDto> updatePayment(@PathVariable Integer id, @RequestBody PaymentDto paymentDto) {
        PaymentDto updatedPayment = paymentService.updatePayment(id, paymentDto);
        return ResponseEntity.ok(updatedPayment);
    }

    /**
     * Видаляє платіж за його ID.
     * Доступно тільки для ADMIN.
     * Цей ендпоінт відповідає на DELETE /api/payments/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePayment(@PathVariable Integer id) {
        paymentService.deletePayment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}