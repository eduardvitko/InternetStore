package com.example.InternetStore.controller;


import com.example.InternetStore.dto.CategoryDto;
import com.example.InternetStore.dto.ProductDto;
import com.example.InternetStore.reposietories.CategoryRepository;
import com.example.InternetStore.reposietories.ProductRepository;
import com.example.InternetStore.services.CategoryServise;
import com.example.InternetStore.services.ProductServise;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductServise productServise;

    public ProductController(ProductServise productServise) {

        this.productServise= productServise;
    }
    @GetMapping("/all")
    public List<ProductDto> getAllProducts() {
        return productServise.getAllProducts() ;
    }
    @GetMapping("/by-category/{categoryId}")
    public List<ProductDto> getProductsByCategory(@PathVariable Integer categoryId) {
        return productServise.getProductsByCategory(categoryId);
    }
}

