package com.example.InternetStore.controller;

import com.example.InternetStore.dto.CategoryDto;
import com.example.InternetStore.dto.OrderDto;
import com.example.InternetStore.dto.ProductDto;
import com.example.InternetStore.dto.UserDto;
import com.example.InternetStore.model.Category;
import com.example.InternetStore.model.Order;
import com.example.InternetStore.model.Product;
import com.example.InternetStore.model.User;
import com.example.InternetStore.services.CategoryServise;
import com.example.InternetStore.services.OrderService;
import com.example.InternetStore.services.ProductServise;
import com.example.InternetStore.services.UserService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {


    private final UserService userService;
    private final CategoryServise categoryServise;
    private final ProductServise productServise;
    private final OrderService orderServiсe;

    public AdminController(UserService userService, CategoryServise categoryServise,ProductServise productServise, OrderService orderServiсe) {

        this.userService = userService;
        this.categoryServise = categoryServise;
        this.productServise = productServise;
        this.orderServiсe = orderServiсe;
    }
    //USER - CONTROLLERS

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDto userDto) {
        try {
            // Викличте метод сервісу, який тепер обробляє перетворення DTO на сутність
            UserDto createdUserDto = userService.create(userDto); // Очікуємо UserDto у відповідь
            return ResponseEntity.ok(createdUserDto); // Поверніть UserDto
        } catch (ConstraintViolationException ex) {
            List<String> errors = ex.getConstraintViolations().stream()
                    .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Помилка створення користувача: " + e.getMessage(), e);
        }
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

    //CATEGORY - CONTROLLER

    @GetMapping("/all/categories")
    public List<CategoryDto> getAllCategories() {
        return categoryServise.getAllCategories();
    }

    @DeleteMapping("/delete/category/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id) {
        if (!categoryServise.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        categoryServise.deleteById(id);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/update/category/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Integer id, @RequestBody Category updateCategory) {
        return categoryServise.updateCategory(id, updateCategory)
                .map(category -> ResponseEntity.ok().body("Category updated"))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping("/create/category")
    public Category createCategory(@RequestBody Category category) {
        return categoryServise.create(category);
    }

    //PRODUCTS - CONTROLLER

    @GetMapping("/all/products")
    public List<ProductDto> getAllProducts() {
        return productServise.getAllProducts() ;
    }
    @PostMapping("/create/product")
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto dto) {
        Product created = productServise.createProduct(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/update/product/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @RequestBody ProductDto updatedProduct) {
        return productServise.updateProduct(id, updatedProduct)
                .map(product -> ResponseEntity.ok("Продукт оновлено"))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/product/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Integer id) {
        return productServise.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/delete/product/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {
        if (!productServise.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
     productServise.deleteById(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderDto>> getAllOrdersWithUserAndAddress() {
        List<OrderDto> orders = orderServiсe.getAllOrdersWithUserAndAddress();
        return ResponseEntity.ok(orders);
    }
}

