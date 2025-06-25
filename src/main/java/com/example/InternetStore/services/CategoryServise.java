package com.example.InternetStore.services;

import com.example.InternetStore.reposietories.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryServise {
    private final CategoryRepository categoryRepository;
    public CategoryServise(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }
}
