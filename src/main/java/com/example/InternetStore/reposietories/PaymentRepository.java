package com.example.InternetStore.reposietories;

import com.example.InternetStore.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    // Отримати всі платежі за конкретним замовленням
    List<Payment> findByOrderId(Integer orderId);
    List<Payment> findByOrderIdIn(List<Integer> orderIds);

}
