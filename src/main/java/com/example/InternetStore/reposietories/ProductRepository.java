package com.example.InternetStore.reposietories;

import com.example.InternetStore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Object> findById(int id);
    List<Object> findAllBy();
    boolean existsByName(String name);
}
