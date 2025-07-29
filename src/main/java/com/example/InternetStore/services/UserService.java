package com.example.InternetStore.services;

import com.example.InternetStore.dto.UserDto;
import com.example.InternetStore.model.Role;
import com.example.InternetStore.model.User;
import com.example.InternetStore.reposietories.UserRepository;
import com.example.InternetStore.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        // Мапимо DTO в Entity
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPhone(userDto.getPhone());
        // якщо треба, додайте мапінг ролей або інших полів

        User savedUser = userRepository.save(user);

        // Повертаємо збережений User як UserDto (опціонально)
        return new UserDto(savedUser.getId(), savedUser.getUsername(), savedUser.getPhone(), /*roles*/ null);
    }
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


}

