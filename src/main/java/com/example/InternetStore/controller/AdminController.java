package com.example.InternetStore.controller;

import com.example.InternetStore.dto.CategoryDto;
import com.example.InternetStore.dto.ProductDto;
import com.example.InternetStore.dto.UserDto;
import com.example.InternetStore.model.Category;
import com.example.InternetStore.model.Product;
import com.example.InternetStore.model.User;
import com.example.InternetStore.services.CategoryServise;
import com.example.InternetStore.services.ProductServise;
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
    private ProductServise productServise;

    public AdminController(UserService userService, CategoryServise categoryServise,ProductServise productServise) {

        this.userService = userService;
        this.categoryServise = categoryServise;
        this.productServise = productServise;
    }
    //USER - CONTROLLERS

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
}

