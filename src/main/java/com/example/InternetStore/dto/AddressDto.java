package com.example.InternetStore.dto;

import com.example.InternetStore.model.Address;
import com.example.InternetStore.model.User;
import lombok.Data;

@Data
public class AddressDto {
    private Integer id;
    private Integer userId;
    private String country;
    private String city;
    private String street;
    private String houseNumber;
    private String apartmentNumber;
    private String postalCode;
    private String region;
    private Boolean isDefault;
    // Маппінг: Entity → DTO
    public static AddressDto fromEntity(Address address) {
        AddressDto dto = new AddressDto();
        dto.setId(address.getId());
        dto.setUserId(address.getUser().getId());
        dto.setCountry(address.getCountry());
        dto.setCity(address.getCity());
        dto.setStreet(address.getStreet());
        dto.setHouseNumber(address.getHouseNumber());
        dto.setApartmentNumber(address.getApartmentNumber());
        dto.setPostalCode(address.getPostalCode());
        dto.setRegion(address.getRegion());
        dto.setIsDefault(address.getIsDefault());
        return dto;
    }

    // Маппінг: DTO → Entity
    public Address toEntity(User user) {
        return Address.builder()
                .id(this.id)
                .user(user)
                .country(this.country)
                .city(this.city)
                .street(this.street)
                .houseNumber(this.houseNumber)
                .apartmentNumber(this.apartmentNumber)
                .postalCode(this.postalCode)
                .region(this.region)
                .isDefault(this.isDefault != null ? this.isDefault : false)
                .build();
    }
}
