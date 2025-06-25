package com.example.InternetStore.reposietories;

import com.example.InternetStore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Object> findById(int id);
}
