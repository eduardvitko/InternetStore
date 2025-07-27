package com.example.InternetStore.services;


import com.example.InternetStore.dto.CategoryDto;
import com.example.InternetStore.model.Category;
import com.example.InternetStore.reposietories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiсe {

    private final CategoryRepository categoryRepository;

    public CategoryServiсe(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryDto::new)
                .toList();
    }

    public void deleteById(int id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        } else {
            throw new RuntimeException("Category with ID " + id + " not found");
        }
    }

    public boolean existsById(int id){
        return categoryRepository.existsById(id);
    };

    public Optional<Category> updateCategory(Integer id, Category updateCategory) {
        return categoryRepository.findById(id).map(existingCategory -> {
            existingCategory.setName(updateCategory.getName());
            return categoryRepository.save(existingCategory);
        });
    }
    public Category create(Category category) {
        if (category.getName() == null || category.getName().isBlank()) {
            throw new IllegalArgumentException("Название категории не может быть пустым");
        }

        if (categoryRepository.existsByName(category.getName())) {
            throw new IllegalArgumentException("Категория с таким названием уже существует");
        }

        return categoryRepository.save(category);
    }

    }
