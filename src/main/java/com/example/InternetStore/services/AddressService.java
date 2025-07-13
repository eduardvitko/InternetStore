package com.example.InternetStore.services;

import com.example.InternetStore.dto.AddressDto;
import com.example.InternetStore.model.Address;
import com.example.InternetStore.model.User;
import com.example.InternetStore.reposietories.AddressRepository;
import com.example.InternetStore.reposietories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public List<AddressDto> getUserAddresses(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return addressRepository.findByUser(user).stream()
                .map(AddressDto::fromEntity)
                .collect(Collectors.toList());
    }

    public AddressDto createAddress(AddressDto dto) {
        if (dto.getUserId() == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id " + dto.getUserId()));

        var address = dto.toEntity(user);
        return AddressDto.fromEntity(addressRepository.save(address));
    }


    public void deleteAddress(Integer id) {
        if (!addressRepository.existsById(id)) {
            throw new RuntimeException("Address with id " + id + " not found");
        }
        addressRepository.deleteById(id);
    }
    public AddressDto updateAddress(Integer id, AddressDto dto) {
        Address existing = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + id));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));

        // Оновлення полів
        existing.setCountry(dto.getCountry());
        existing.setCity(dto.getCity());
        existing.setStreet(dto.getStreet());
        existing.setHouseNumber(dto.getHouseNumber());
        existing.setApartmentNumber(dto.getApartmentNumber());
        existing.setPostalCode(dto.getPostalCode());
        existing.setRegion(dto.getRegion());
        existing.setIsDefault(dto.getIsDefault() != null ? dto.getIsDefault() : false);
        existing.setUser(user); // на випадок, якщо змінено userId
        existing.setUpdatedAt(LocalDateTime.now());

        Address updated = addressRepository.save(existing);
        return AddressDto.fromEntity(updated);
    }



}
