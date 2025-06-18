package com.example.InternetStore.controller;

import com.example.InternetStore.model.User;
import com.example.InternetStore.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return service.create(user);
    }
    @GetMapping
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }
}
