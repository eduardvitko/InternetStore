package com.example.InternetStore.reposietories;

import com.example.InternetStore.dto.ProductDto;
import com.example.InternetStore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findById(int id);
    List<Product> findAllBy();
    boolean existsByName(String name);
    List<Product> findByCategoryId(Integer categoryId);


    @Query("SELECT DISTINCT p FROM Product p " +
            "LEFT JOIN FETCH p.category " +
            "LEFT JOIN FETCH p.images")
    List<Product> findAllWithCategoryAndImages();

    @Query("SELECT DISTINCT p FROM Product p " +
            "LEFT JOIN FETCH p.category " +
            "LEFT JOIN FETCH p.images " +
            "WHERE p.category.id = :categoryId")
    List<Product> findByCategoryIdWithImages(@Param("categoryId") Integer categoryId);
   @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Product> findByNameContainingIgnoreCase(@Param("query") String query);


    @Query("SELECT new com.example.InternetStore.dto.ProductDto(p.id, p.name, p.price) FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<ProductDto> searchProducts(@Param("query") String query);
    @Query("SELECT DISTINCT p FROM Product p " +
            "LEFT JOIN FETCH p.category " +
            "LEFT JOIN FETCH p.images " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Product> searchProductsWithCategoryAndImages(@Param("query") String query);



}

