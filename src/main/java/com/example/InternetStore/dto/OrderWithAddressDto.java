package com.example.InternetStore.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderWithAddressDto {
    private Integer id;
    private LocalDateTime orderDate;
    private String status;
    private BigDecimal total;
    private AddressDto address;

    private String username; // 👈 NEW
    private String phone;    // 👈 NEW
}
