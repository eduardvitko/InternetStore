package com.example.InternetStore.dto;

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
}
