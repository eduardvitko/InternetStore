package com.example.InternetStore.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Builder
@Getter
@Entity
@Table(name = "payment")
public class Payment {

    // Getters і Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Використовуйте Integer для AUTO_INCREMENT PRIMARY KEY

    @Column(name = "order_id", nullable = false)
    private Integer orderId; // Відповідає INT NOT NULL

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate; // Відповідає DATETIME DEFAULT CURRENT_TIMESTAMP

    @Column(name = "amount", nullable = false)
    private BigDecimal amount; // Відповідає DECIMAL(10, 2) NOT NULL

    @Enumerated(EnumType.STRING) // Важливо! Це вказує JPA, що потрібно зберігати Enum як String
    @Column(name = "method", nullable = false)
    private PaymentMethod method; // Ваш Java Enum

    // Конструктори (якщо потрібні)
    public Payment() {
        // Конструктор за замовчуванням
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }
    public Integer getOrderId() {
        return orderId;
    }
    public Integer getId() {
        return id;
    }

    // ... інші методи, такі як equals(), hashCode(), toString()
}
