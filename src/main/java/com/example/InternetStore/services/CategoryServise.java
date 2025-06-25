package com.example.InternetStore.services;


import com.example.InternetStore.dto.CategoryDto;
import com.example.InternetStore.dto.UserDto;
import com.example.InternetStore.model.Category;
import com.example.InternetStore.reposietories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CategoryServise {
    private final CategoryRepository categoryRepository;

    public CategoryServise(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryDto::new)
                .toList();
    }

    }
