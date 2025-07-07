package com.example.InternetStore.dto;

import java.util.List;

public class SearchResultDto {
    private List<ProductDto> foundProducts;
    private List<ProductDto> productsInCategory;

    public SearchResultDto(List<ProductDto> foundProducts, List<ProductDto> productsInCategory) {
        this.foundProducts = foundProducts;
        this.productsInCategory = productsInCategory;
    }

    public List<ProductDto> getFoundProducts() {
        return foundProducts;
    }

    public void setFoundProducts(List<ProductDto> foundProducts) {
        this.foundProducts = foundProducts;
    }

    public List<ProductDto> getProductsInCategory() {
        return productsInCategory;
    }

    public void setProductsInCategory(List<ProductDto> productsInCategory) {
        this.productsInCategory = productsInCategory;
    }
}
