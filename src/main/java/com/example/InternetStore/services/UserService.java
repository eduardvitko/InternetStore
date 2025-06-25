package com.example.InternetStore.services;

import com.example.InternetStore.dto.UserDto;
import com.example.InternetStore.model.Role;
import com.example.InternetStore.model.User;
import com.example.InternetStore.reposietories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {
        return userRepository.save(user);
    }
    public Set<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    Set<String> roles = user.getRoles().stream()
                            .map(Role::getName)
                            .collect(Collectors.toSet());
                    return new UserDto(user.getUsername(), user.getPhone(), roles);
                })
                .collect(Collectors.toSet());
}
}