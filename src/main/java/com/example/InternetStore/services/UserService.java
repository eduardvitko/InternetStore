package com.example.InternetStore.services;

import com.example.InternetStore.dto.UserDto;
import com.example.InternetStore.model.Role;
import com.example.InternetStore.model.User;
import com.example.InternetStore.reposietories.RoleRepository;
import com.example.InternetStore.reposietories.UserRepository;
import com.example.InternetStore.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository,RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

//    public User create(User user) {
//        return userRepository.save(user);
//    }
    public Set<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    Set<String> roles = user.getRoles().stream()
                            .map(Role::getName)
                            .collect(Collectors.toSet());
                    return new UserDto(user.getId(),user.getUsername(), user.getPhone(), roles);
                })
                .collect(Collectors.toSet());
}
    public void deleteById(int id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User with ID " + id + " not found");
        }
    }
    public boolean existsById(int id){
        return userRepository.existsById(id);
    };
    public Optional<User> updateUser(Integer id, User updatedUser) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setPhone(updatedUser.getPhone());
            // обнови роли, если нужно:
            existingUser.setRoles(updatedUser.getRoles());
            return userRepository.save(existingUser);
        });
    }
        public Optional<User> getCurrentUser() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.empty();
            }

            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails customUserDetails) {
                return Optional.of(customUserDetails.getUser());
            }

            return Optional.empty();
        }
    public UserDto create(UserDto userDto) {
        // ↓↓↓ ОСЬ НОВА ЛОГІКА ПЕРЕВІРКИ ↓↓↓

        // 1. Перевіряємо, чи існує користувач з таким ім'ям
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Користувач з таким ім'ям вже існує.");
        }

        // 2. Перевіряємо, чи існує користувач з таким телефоном
        if (userRepository.findByPhone(userDto.getPhone()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Користувач з таким номером телефону вже існує.");
        }

        // --- Ваша існуюча логіка створення користувача ---
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPhone(userDto.getPhone());
        // Важливо: пароль має бути захешований перед збереженням!
        // Ви маєте передавати пароль у DTO при реєстрації.
        // user.setPasswordHash(passwordEncoder.encode(userDto.getPassword()));

        // Присвоюємо роль за замовчуванням (наприклад, 'USER')
        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> roleRepository.save(new Role("USER")));
        user.setRoles(Set.of(userRole));

        User savedUser = userRepository.save(user);

        return new UserDto(savedUser); // Використовуємо конструктор, який ви вже маєте
    }



}

