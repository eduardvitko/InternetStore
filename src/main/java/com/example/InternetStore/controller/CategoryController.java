package com.example.InternetStore.controller;

import com.example.InternetStore.dto.CategoryDto;
import com.example.InternetStore.reposietories.CategoryRepository;
import com.example.InternetStore.services.CategoryServiсe;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private CategoryRepository categoryRepository;
    private CategoryServiсe categoryServiсe;

    public CategoryController(CategoryRepository categoryRepository, CategoryServiсe categoryServiсe) {
        this.categoryRepository = categoryRepository;
        this.categoryServiсe = categoryServiсe;
    }

    @GetMapping("/all")
    public List<CategoryDto> getAllCategories() {
        return categoryServiсe.getAllCategories() ;
    }
}