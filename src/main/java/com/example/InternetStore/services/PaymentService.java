// src/main/java/com/example/InternetStore/services/PaymentService.java

package com.example.InternetStore.services;

import com.example.InternetStore.dto.PaymentDto;
import com.example.InternetStore.model.Order;
import com.example.InternetStore.model.Payment;
import com.example.InternetStore.reposietories.OrderRepository;
import com.example.InternetStore.reposietories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    // Створити новий платіж
    @Transactional
    public PaymentDto createPayment(PaymentDto dto) {
        // Знайти замовлення за його ID
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Створити об'єкт Payment за допомогою Builder патерну
        // Змінено .order(order) на .orderId(order.getId())
        Payment payment = Payment.builder()
                .orderId(order.getId()) // Встановлюємо orderId з об'єкта Order
                .paymentDate(dto.getPaymentDate())
                .amount(dto.getAmount())
                .method(dto.getMethod())
                .build();

        // Зберегти платіж у репозиторії
        Payment savedPayment = paymentRepository.save(payment);

        // Повернути DTO збереженого платежу
        return toDto(savedPayment);
    }

    // Отримати всі платежі
    @Transactional(readOnly = true)
    public List<PaymentDto> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Метод для перетворення сутності Payment на PaymentDto
    private PaymentDto toDto(Payment payment) {
        return new PaymentDto(
                payment.getId(),
                payment.getOrderId(),
                payment.getPaymentDate(),
                payment.getAmount(),
                payment.getMethod()
        );
    }
}
