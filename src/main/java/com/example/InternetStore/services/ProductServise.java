package com.example.InternetStore.services;
import com.example.InternetStore.dto.CategoryDto;
import com.example.InternetStore.dto.ProductDto;
import com.example.InternetStore.model.Category;
import com.example.InternetStore.model.Product;
import com.example.InternetStore.reposietories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class ProductServise {

    private ProductRepository productRepository;

    public ProductServise(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public List<ProductDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductDto::new)  // теперь вызовет созданный конструктор
                .collect(Collectors.toList());
    }
    public void deleteById(int id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new RuntimeException("Product with ID " + id + " not found");
        }
    }

    public boolean existsById(int id){
        return productRepository.existsById(id);
    };
    public Optional<Product> updateProduct(Integer id, Product updateProduct) {
        return productRepository.findById(id).map(existingProduct -> {
            existingProduct.setName(updateProduct.getName());
            existingProduct.setDescription(updateProduct.getDescription());
            existingProduct.setPrice(updateProduct.getPrice());
            existingProduct.setStock(updateProduct.getStock());
            return productRepository.save(existingProduct);
        });
    }
    public Product createProduct(Product product) {
        if (product.getName() == null || product.getName().isBlank()) {
            throw new IllegalArgumentException("Название продукта не может быть пустым");
        }

        if (productRepository.existsByName(product.getName())) {
            throw new IllegalArgumentException("Продуктс таким названием уже существует");
        }

        return productRepository.save(product);
    }

}
