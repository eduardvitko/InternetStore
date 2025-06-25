package com.example.InternetStore.controller;

import com.example.InternetStore.dto.UserDto;
import com.example.InternetStore.model.User;
import com.example.InternetStore.reposietories.UserRepository;
import com.example.InternetStore.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService service;
    private UserRepository userRepository;
    public UserController(UserService service, UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }


    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = ((UserDetails) auth.getPrincipal()).getUsername();

        User user = (User) userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Преобразуем роли в строки (например, "USER", "ADMIN")
        Set<String> roleNames = user.getRoles()
                .stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet());

        UserDto response = new UserDto(user.getId(),user.getUsername(), user.getPhone(), roleNames);

        return ResponseEntity.ok(response);
    }




}
