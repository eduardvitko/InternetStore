package com.example.InternetStore.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;
@Data

public class UserDto {
    private String username;
    private String phone;
    private Set<String> roles;

    public UserDto(String username, String phone, Set<String> roles) {
        this.username = username;
        this.phone = phone;
        this.roles = roles;
    }

    // Getters
}

