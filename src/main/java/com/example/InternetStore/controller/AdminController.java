package com.example.InternetStore.controller;

import com.example.InternetStore.dto.UserDto;
import com.example.InternetStore.model.User;
import com.example.InternetStore.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService service;

    public AdminController(UserService service) {
        this.service = service;
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        return service.create(user);
    }

    @GetMapping("/users")
    public Set<UserDto> getAllUsers() {
        return service.getAllUsers();
    }
}

