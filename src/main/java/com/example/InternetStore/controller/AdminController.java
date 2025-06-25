package com.example.InternetStore.controller;

import com.example.InternetStore.dto.CategoryDto;
import com.example.InternetStore.dto.UserDto;
import com.example.InternetStore.model.User;
import com.example.InternetStore.services.CategoryServise;
import com.example.InternetStore.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {


    private final UserService userService;
    private final CategoryServise categoryServise;

    public AdminController(UserService userService, CategoryServise categoryServise) {

        this.userService = userService;
        this.categoryServise = categoryServise;
    }

    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        return userService.create(user);
    }

    @GetMapping("/users")
    public Set<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        if (!userService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser)
                .map(user -> ResponseEntity.ok().body("User updated"))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/all/categories")
    public List<CategoryDto> getAllCategories() {
        return categoryServise.getAllCategories();
    }
}

