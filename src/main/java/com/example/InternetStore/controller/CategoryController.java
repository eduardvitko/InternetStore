package com.example.InternetStore.controller;

import com.example.InternetStore.dto.CategoryDto;
import com.example.InternetStore.dto.UserDto;
import com.example.InternetStore.reposietories.CategoryRepository;
import com.example.InternetStore.services.CategoryServise;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private CategoryRepository categoryRepository;
    private  CategoryServise categoryServise;

    public CategoryController(CategoryRepository categoryRepository, CategoryServise categoryServise) {
        this.categoryRepository = categoryRepository;
        this.categoryServise = categoryServise;
    }

    @GetMapping("/all")
    public List<CategoryDto> getAllCategories() {
        return categoryServise.getAllCategories() ;
    }
}