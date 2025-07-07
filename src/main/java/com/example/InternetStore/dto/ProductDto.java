package com.example.InternetStore.dto;

import com.example.InternetStore.model.Product;
import com.example.InternetStore.dto.ImageDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDto {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Integer categoryId;
    private String categoryName; // опционально, если хочешь сразу имя категории
    private List<ImageDto> images;


    public ProductDto() {
    }
    public ProductDto(Integer id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }


    public ProductDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.stock = product.getStock();
        if (product.getCategory() != null) {
            this.categoryId = product.getCategory().getId();
            this.categoryName = product.getCategory().getName();
        }

        // 🔥 Додай ось це — трансформуємо список Image → ImageDto
        if (product.getImages() != null) {
            this.images = product.getImages().stream()
                    .map(img -> new ImageDto(
                            img.getId(),
                            img.getUrl(),
                            img.getAltText(),
                            product.getId()))
                    .collect(Collectors.toList());
        }
    }




    // Геттеры и сеттеры
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public  void setImages(List<ImageDto>images){
        this.images = images;
    }
    public List<ImageDto> getImages() {
        return images;
    }
}
