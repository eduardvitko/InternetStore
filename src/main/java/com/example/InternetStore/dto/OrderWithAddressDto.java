package com.example.InternetStore.dto;

import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Data
public class OrderWithAddressDto {
    private Integer id;
    private LocalDateTime orderDate;
    private BigDecimal total;
    private String status;

    private AddressDto address;

}
