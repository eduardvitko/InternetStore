package com.example.InternetStore.services;
import com.example.InternetStore.dto.CategoryDto;
import com.example.InternetStore.dto.ProductDto;
import com.example.InternetStore.model.Category;
import com.example.InternetStore.model.Product;
import com.example.InternetStore.reposietories.CategoryRepository;
import com.example.InternetStore.reposietories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class ProductServise {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    public ProductServise(ProductRepository productRepository,CategoryRepository categoryRepository){
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
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
    public Optional <ProductDto> getProductById(Integer id) {
        return productRepository.findById(id)
                .map(product -> {
                    ProductDto dto = new ProductDto();
                    dto.setId(product.getId());
                    dto.setName(product.getName());
                    dto.setDescription(product.getDescription());
                    dto.setPrice(product.getPrice());
                    dto.setStock(product.getStock());
                    dto.setCategoryName(product.getCategory().getName());
                    if (product.getCategory() != null) {
                        dto.setCategoryId(product.getCategory().getId());
                        dto.setCategoryName(product.getCategory().getName());
                    }
                    return dto;
                });
    }
    public Product createProduct(ProductDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Категорія не знайдена"));
            product.setCategory(category);
        }

        return productRepository.save(product);
    }

    public Optional<Product> updateProduct(Integer id, Product updatedProduct) {
        return productRepository.findById(id).map(existingProduct -> {
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setStock(updatedProduct.getStock());

            // Если нужно обновлять категорию:
            if (updatedProduct.getCategory() != null) {
                existingProduct.setCategory(updatedProduct.getCategory());
            }

            return productRepository.save(existingProduct);
        });
    }

}
