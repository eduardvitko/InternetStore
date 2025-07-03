

package com.example.InternetStore.controller;

import com.example.InternetStore.dto.PaymentDto;
import com.example.InternetStore.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Позначає клас як REST-контролер
@RequestMapping("/api/payments") // Базовий шлях для всіх ендпоінтів у цьому контролері
@RequiredArgsConstructor // Генерує конструктор з усіма final полями для ін'єкції залежностей
public class PaymentController {

    private final PaymentService paymentService; // Ін'єкція сервісу для бізнес-логіки

    /**
     * Створює новий платіж.
     * Доступно тільки для користувачів з роллю ADMIN.
     * @param paymentDto Об'єкт PaymentDto, що містить дані для створення платежу.
     * @return ResponseEntity з PaymentDto створеного платежу та статусом HttpStatus.CREATED.
     */
    @PostMapping // Обробляє POST-запити на /api/payments
    @PreAuthorize("hasRole('ADMIN')") // Тільки ADMIN може створювати платежі
    public ResponseEntity<PaymentDto> createPayment(@RequestBody PaymentDto paymentDto) {
        PaymentDto createdPayment = paymentService.createPayment(paymentDto);
        return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
    }

    /**
     * Отримує платіж за його ID.
     * Доступно для користувачів з роллю ADMIN або для користувача, який володіє замовленням, пов'язаним з платежем.
     * (Примітка: для перевірки володіння замовленням потрібна додаткова логіка в сервісі).
     * @param id ID платежу.
     * @return ResponseEntity з PaymentDto знайденого платежу та статусом HttpStatus.OK,
     * або HttpStatus.NOT_FOUND, якщо платіж не знайдено.
     */
    @GetMapping("/{id}") // Обробляє GET-запити на /api/payments/{id}
    @PreAuthorize("hasRole('ADMIN') or @paymentService.isPaymentOwnedByUser(#id, authentication.name)") // Приклад: ADMIN або власник
    public ResponseEntity<PaymentDto> getPaymentById(@PathVariable Integer id) {
        PaymentDto payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(payment); // Автоматично поверне 404, якщо сервіс викине EntityNotFoundException
    }

    /**
     * Отримує список усіх платежів.
     * Доступно тільки для користувачів з роллю ADMIN.
     * @return ResponseEntity зі списком PaymentDto та статусом HttpStatus.OK.
     */
    @GetMapping // Обробляє GET-запити на /api/payments
    @PreAuthorize("hasRole('ADMIN')") // Тільки ADMIN може бачити всі платежі
    public ResponseEntity<List<PaymentDto>> getAllPayments() {
        List<PaymentDto> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    /**
     * Оновлює існуючий платіж.
     * Доступно тільки для користувачів з роллю ADMIN.
     * @param id ID платежу для оновлення.
     * @param paymentDto Об'єкт PaymentDto з оновленими даними.
     * @return ResponseEntity з оновленим PaymentDto та статусом HttpStatus.OK,
     * або HttpStatus.NOT_FOUND, якщо платіж не знайдено.
     */
    @PutMapping("/{id}") // Обробляє PUT-запити на /api/payments/{id}
    @PreAuthorize("hasRole('ADMIN')") // Тільки ADMIN може оновлювати платежі
    public ResponseEntity<PaymentDto> updatePayment(@PathVariable Integer id, @RequestBody PaymentDto paymentDto) {
        PaymentDto updatedPayment = paymentService.updatePayment(id, paymentDto);
        return ResponseEntity.ok(updatedPayment);
    }

    /**
     * Видаляє платіж за його ID.
     * Доступно тільки для користувачів з роллю ADMIN.
     * @param id ID платежу для видалення.
     * @return ResponseEntity без тіла та статусом HttpStatus.NO_CONTENT.
     */
    @DeleteMapping("/{id}") // Обробляє DELETE-запити на /api/payments/{id}
    @PreAuthorize("hasRole('ADMIN')") // Тільки ADMIN може видаляти платежі
    public ResponseEntity<Void> deletePayment(@PathVariable Integer id) {
        paymentService.deletePayment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
    }
}
