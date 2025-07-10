package com.example.InternetStore.controller;

import com.example.InternetStore.dto.AddressDto;
import com.example.InternetStore.services.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/user/{userId}")
    public List<AddressDto> getUserAddresses(@PathVariable Integer userId) {
        return addressService.getUserAddresses(userId);
    }

    @PostMapping
    public AddressDto createAddress(@RequestBody AddressDto dto) {
        return addressService.createAddress(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable Integer id) {
        addressService.deleteAddress(id);
    }
    @PutMapping("/{id}")
    public AddressDto updateAddress(@PathVariable Integer id, @RequestBody AddressDto dto) {
        return addressService.updateAddress(id, dto);
    }

}
