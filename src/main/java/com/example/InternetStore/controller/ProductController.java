package com.example.InternetStore.controller;


import com.example.InternetStore.dto.CategoryDto;
import com.example.InternetStore.dto.ProductDto;
import com.example.InternetStore.dto.SearchResultDto;
import com.example.InternetStore.reposietories.CategoryRepository;
import com.example.InternetStore.reposietories.ProductRepository;
import com.example.InternetStore.services.CategoryServise;
import com.example.InternetStore.services.ProductServise;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable Integer id) {
        return productServise.getProductById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не знайдено"));
    }
    @GetMapping("/search-with-category")
    public SearchResultDto searchWithCategory(@RequestParam("q") String query) {
        List<ProductDto> foundProducts = productServise.searchProducts(query);

        if (foundProducts.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Товар не знайдено");
        }

        // Беремо категорію першого знайденого товару
        Integer categoryId = foundProducts.get(0).getCategoryId();

        // Отримуємо всі товари цієї категорії
        List<ProductDto> productsInCategory = productServise.getProductsByCategory(categoryId);

        // Формуємо відповідь
        return new SearchResultDto(foundProducts, productsInCategory);
    }

    @GetMapping("/search")
    public List<ProductDto> searchProducts(@RequestParam("q") String query) {
        return productServise.searchProducts(query);
    }
}

