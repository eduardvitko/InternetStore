package com.example.InternetStore.dto;

public class ImageDto {
    private Integer id;
    private String url;
    private String altText;
    private Integer productId;

    public ImageDto() {}

    public ImageDto(Integer id, String url, String altText, Integer productId) {
        this.id = id;
        this.url = url;
        this.altText = altText;
        this.productId = productId;
    }

    // --- Геттери та сеттери ---
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}
