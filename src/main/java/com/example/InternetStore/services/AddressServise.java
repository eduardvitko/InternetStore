package com.example.InternetStore.services;

import com.example.InternetStore.dto.AddressDto;
import com.example.InternetStore.model.User;
import com.example.InternetStore.reposietories.AddressRepository;
import com.example.InternetStore.reposietories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServise {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public List<AddressDto> getUserAddresses(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return addressRepository.findByUser(user).stream()
                .map(AddressDto::fromEntity)
                .collect(Collectors.toList());
    }

    public AddressDto createAddress(AddressDto dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow();
        var address = dto.toEntity(user);
        return AddressDto.fromEntity(addressRepository.save(address));
    }

}
