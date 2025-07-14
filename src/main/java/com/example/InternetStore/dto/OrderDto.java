package com.example.InternetStore.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String username;       // 👈 нове
    private String phone;          // 👈 нове

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime orderDate;

    private String status;
    private BigDecimal total;

    private AddressDto address;    // 👈 нове
    private List<OrderItemDto> items;

}
