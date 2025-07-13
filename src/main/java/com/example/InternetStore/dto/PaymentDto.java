package com.example.InternetStore.dto;

import com.example.InternetStore.model.Payment;
import com.example.InternetStore.model.PaymentMethod;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDto {
    private Integer id;
    private Integer orderId;
    private LocalDateTime paymentDate;
    private BigDecimal amount;
    private PaymentMethod method;

    public PaymentDto toDto(Payment payment) {
        if (payment == null) return null;

        return PaymentDto.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .paymentDate(payment.getPaymentDate())
                .amount(payment.getAmount())
                .method(payment.getMethod())
                .build();
    }

}
