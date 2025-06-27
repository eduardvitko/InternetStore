package com.example.InternetStore.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {

    private Integer id;
    private Integer userId;
    private LocalDateTime orderDate;
    private String status;
    private BigDecimal total;
    private List<OrderItemDto> items;
}
