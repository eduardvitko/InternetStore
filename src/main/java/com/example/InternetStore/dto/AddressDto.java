package com.example.InternetStore.dto;

import lombok.Data;

@Data
public class AddressDto {
    private Long id;
    private Long userId;
    private String country;
    private String city;
    private String street;
    private String houseNumber;
    private String apartmentNumber;
    private String postalCode;
    private String region;
    private Boolean isDefault;
}
