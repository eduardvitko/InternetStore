package com.example.InternetStore.dto;


import com.example.InternetStore.model.Role;
import com.example.InternetStore.model.User;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data

public class UserDto {
    private Integer id;

    @Pattern(regexp = "^[a-zA-Zа-яА-ЯіїєІЇЄґҐ]+$", message = "Ім’я користувача може містити лише літери українського або англійського алфавіту")
    private String username;

    @Pattern(regexp = "^\\+?\\d+$", message = "Телефон має містити тільки цифри та може починатись з +")
    private String phone;
    private Set<String> roles;

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.phone = user.getPhone();
        this.roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
    }
    public UserDto(Integer id, String username, String phone, Set<String> roles) {
        this.id = id;
        this.username = username;
        this.phone = phone;
        this.roles = roles;
    }


    public Integer getId(){return id;}
    // Getters
}

