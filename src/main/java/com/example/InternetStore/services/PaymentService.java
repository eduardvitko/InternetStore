// src/main/java/com/example/InternetStore/services/PaymentService.java

package com.example.InternetStore.services;

import com.example.InternetStore.dto.PaymentDto;
import com.example.InternetStore.model.Order;
import com.example.InternetStore.model.Payment;
import com.example.InternetStore.model.User;
import com.example.InternetStore.reposietories.OrderRepository;
import com.example.InternetStore.reposietories.PaymentRepository;
import com.example.InternetStore.reposietories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;



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
    /**
     * Отримує платіж за його ID.
     *
     * @param id ID платежу.
     * @return PaymentDto знайденого платежу.
     * @throws EntityNotFoundException якщо платіж з вказаним ID не знайдено.
     */
    @Transactional(readOnly = true)
    public PaymentDto getPaymentById(Integer id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment with ID " + id + " not found"));
        return toDto(payment);
    }



    /**
     * Оновлює існуючий платіж.
     *
     * @param id ID платежу для оновлення.
     * @param paymentDto Об'єкт PaymentDto з оновленими даними.
     * @return Оновлений PaymentDto.
     * @throws EntityNotFoundException якщо платіж з вказаним ID не знайдено.
     */
    @Transactional
    public PaymentDto updatePayment(Integer id, PaymentDto paymentDto) {
        // Перевіряємо, чи існує платіж
        Payment existingPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment with ID " + id + " not found"));

        // Оновлюємо поля існуючого платежу
        // Примітка: ID замовлення (orderId) зазвичай не змінюється для існуючого платежу.
        // Якщо ви дозволяєте його змінювати, додайте відповідну логіку та перевірки.
        if (paymentDto.getOrderId() != null && !existingPayment.getOrderId().equals(paymentDto.getOrderId())) {
            // Перевірте, чи існує нове замовлення, якщо orderId змінюється
            orderRepository.findById(paymentDto.getOrderId())
                    .orElseThrow(() -> new EntityNotFoundException("New Order with ID " + paymentDto.getOrderId() + " not found"));
            existingPayment.setOrderId(paymentDto.getOrderId());
        }

        if (paymentDto.getPaymentDate() != null) {
            existingPayment.setPaymentDate(paymentDto.getPaymentDate());
        }
        if (paymentDto.getAmount() != null) {
            existingPayment.setAmount(paymentDto.getAmount());
        }
        if (paymentDto.getMethod() != null) {
            existingPayment.setMethod(paymentDto.getMethod());
        }

        // Зберігаємо оновлений платіж
        Payment updatedPayment = paymentRepository.save(existingPayment);
        return toDto(updatedPayment);
    }

    /**
     * Видаляє платіж за його ID.
     *
     * @param id ID платежу для видалення.
     * @throws EntityNotFoundException якщо платіж з вказаним ID не знайдено.
     */
    @Transactional
    public void deletePayment(Integer id) {
        if (!paymentRepository.existsById(id)) {
            throw new EntityNotFoundException("Payment with ID " + id + " not found for deletion");
        }
        paymentRepository.deleteById(id);
    }
    /**
     * Знаходить усі платежі, що належать користувачу з вказаним ім'ям (username).
     * @param username Ім'я користувача (зазвичай email), отримане з токена.
     * @return Список PaymentDto, що належать цьому користувачу.
     */
    public List<PaymentDto> findPaymentsByUsername(String username) {
        // 1. Знаходимо користувача в базі даних за його ім'ям.
        //    Якщо користувача не знайдено, викидаємо виняток.
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Користувача з ім'ям '" + username + "' не знайдено."));

        // 2. Знаходимо всі замовлення, що належать цьому користувачу.
        List<Order> userOrders = orderRepository.findByUser(user);

        // 3. Збираємо ID всіх замовлень цього користувача в один список.
        List<Integer> userOrderIds = userOrders.stream()
                .map(Order::getId)
                .collect(Collectors.toList());

        // Якщо у користувача немає замовлень, то й платежів бути не може. Повертаємо порожній список.
        if (userOrderIds.isEmpty()) {
            return List.of(); // або Collections.emptyList()
        }

        // 4. Знаходимо всі платежі, у яких orderId знаходиться у списку ID замовлень користувача.
        //    Для цього вам потрібно буде додати відповідний метод у ваш PaymentRepository.
        List<Payment> payments = paymentRepository.findByOrderIdIn(userOrderIds);

        // 5. Конвертуємо знайдені сутності Payment у PaymentDto і повертаємо.
        return payments.stream()
                .map(this::convertToDto) // Припускаємо, що у вас є метод для конвертації
                .collect(Collectors.toList());
    }
    private PaymentDto convertToDto(Payment payment) {
        if (payment == null) return null;

        return PaymentDto.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId()) // <-- ТЕПЕР ВИКОРИСТОВУЄМО ПРЯМИЙ ДОСТУП ДО ID
                .paymentDate(payment.getPaymentDate())
                .amount(payment.getAmount())
                .method(payment.getMethod())
                .build();
    }
}
