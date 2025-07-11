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

    private String productName;

    private int quantity;

    private BigDecimal price;




//    public OrderItemDto(Integer id, Integer productId, String productName, int quantity, BigDecimal price) {
//        this.id = id;
//        this.productId = productId;
//        this.productName = productName;
//        this.quantity = quantity;
//        this.price = price;
//    }
}
