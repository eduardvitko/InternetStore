package com.example.InternetStore.services;
import com.example.InternetStore.dto.CategoryDto;
import com.example.InternetStore.dto.ImageDto;
import com.example.InternetStore.dto.ProductDto;
import com.example.InternetStore.model.Category;
import com.example.InternetStore.model.Product;
import com.example.InternetStore.reposietories.CategoryRepository;
import com.example.InternetStore.reposietories.ProductRepository;
import jakarta.transaction.Transactional;
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


    @Transactional
    public void deleteById(int id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new RuntimeException("Product with ID " + id + " not found");
        }
    }
    @Transactional
    public boolean existsById(int id){
        return productRepository.existsById(id);
    };

    @Transactional
    public Optional<ProductDto> getProductById(Integer id) {
        return productRepository.findById(id)
                .map(this::mapToDto); // замість ручного створення dto
    }
    @Transactional
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

    @Transactional // Для операцій запису
    public Optional<ProductDto> updateProduct(Integer id, ProductDto updatedProductDto) {
        return productRepository.findById(id).map(existingProduct -> {
            existingProduct.setName(updatedProductDto.getName());
            existingProduct.setDescription(updatedProductDto.getDescription());
            existingProduct.setPrice(updatedProductDto.getPrice());
            existingProduct.setStock(updatedProductDto.getStock());

            if (updatedProductDto.getCategoryId() != null) {
                Category newCategory = categoryRepository.findById(updatedProductDto.getCategoryId())
                        .orElseThrow(() -> new RuntimeException("Категорію з ID " + updatedProductDto.getCategoryId() + " не знайдено для оновлення продукту."));
                existingProduct.setCategory(newCategory);
            } else {
                // Можливість встановити категорію в null, якщо DTO надсилає null categoryId
                existingProduct.setCategory(null);
            }

            // ВАЖЛИВО: Для зображень зазвичай потрібна окрема логіка для додавання/видалення.
            // Пряме 'setImages' тут з DTO може бути проблематичним для колекцій JPA.
            // Ви, ймовірно, маєте окремий ендпоінт для керування зображеннями, що є правильним підходом.

            Product savedProduct = productRepository.save(existingProduct);
            return mapToDto(savedProduct); // Повернути оновлений DTO
        });
    }
    @Transactional
    public List<ProductDto> getAllProducts() {
        return productRepository.findAllWithCategoryAndImages()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ProductDto> getProductsByCategory(Integer categoryId) {
        List<Product> products = productRepository.findByCategoryIdWithImages(categoryId);
        return products.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductDto mapToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());

        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
        }

        // ***** ПЕРЕКОНАЙТЕСЯ, ЩО ЦІ ЛОГИ ТУТ *****
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            System.out.println("DEBUG: Product ID " + product.getId() + " has " + product.getImages().size() + " images.");
            dto.setImages(product.getImages().stream()
                    .map(image -> {
                        System.out.println("DEBUG:   Mapping Image ID: " + image.getId() + ", URL: " + image.getUrl() + " for product " + product.getId());
                        return new ImageDto(image.getId(), image.getUrl(), image.getAltText(), product.getId());
                    })
                    .collect(Collectors.toList()));
        } else {
            System.out.println("DEBUG: Product ID " + product.getId() + " has NO images or images collection is empty/null.");
        }
        // *****************************************

        return dto;
    }

//    public List<ProductDto> searchProducts(String query) {
//        List<Product> products = productRepository.findByNameContainingIgnoreCase(query);
//        return products.stream()
//                .map(this::toDto)
//                .collect(Collectors.toList());
//    }
public List<ProductDto> searchProducts(String query) {
    // 1. Викликаємо правильний метод репозиторію
    List<Product> foundProducts = productRepository.findByNameContainingIgnoreCase(query);

    // 2. Конвертуємо кожну знайдену сутність Product в ProductDto за допомогою вашого конструктора.
    //    Завдяки @Transactional, всі "ліниві" поля (category, images) будуть доступні.
    return foundProducts.stream()
            .map(ProductDto::new) // Це еквівалентно .map(product -> new ProductDto(product))
            .collect(Collectors.toList());
}

    private ProductDto toDto(Product entity) {
        ProductDto dto = new ProductDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        // додай інші поля за потребою
        return dto;
    }



}
