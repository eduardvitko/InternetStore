package com.example.InternetStore.dto;

import com.example.InternetStore.model.Address;
import com.example.InternetStore.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
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
    // Маппінг: Entity → DTO
    public static AddressDto fromEntity(Address address) {
        if (address == null) return null;
        return AddressDto.builder()
                .id(address.getId())
                .userId(address.getUser() != null ? address.getUser().getId() : null)
                .country(address.getCountry())
                .city(address.getCity())
                .street(address.getStreet())
                .houseNumber(address.getHouseNumber())
                .apartmentNumber(address.getApartmentNumber())
                .postalCode(address.getPostalCode())
                .region(address.getRegion())
                .isDefault(address.getIsDefault())
                .build();
    }
}
