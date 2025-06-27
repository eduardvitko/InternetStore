package com.example.InternetStore.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDto {

    private Integer id;

    private Integer productId;

    private String productName; // Якщо хочеш відображати назву продукту на фронті

    private int quantity;

    private BigDecimal price;
}
