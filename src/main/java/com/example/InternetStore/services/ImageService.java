package com.example.InternetStore.services;

import com.example.InternetStore.dto.ImageDto;
import com.example.InternetStore.model.Image;
import com.example.InternetStore.model.Product;
import com.example.InternetStore.reposietories.ImageRepository;
import com.example.InternetStore.reposietories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository, ProductRepository productRepository) {
        this.imageRepository = imageRepository;
        this.productRepository = productRepository;
    }

    public List<ImageDto> getImagesByProductId(Integer productId) {
        return imageRepository.findByProductId(productId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ImageDto saveImage(ImageDto dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        Image image = new Image(dto.getUrl(), dto.getAltText(), product);
        Image saved = imageRepository.save(image);
        return toDto(saved);
    }

    public ImageDto updateImage(Integer id, ImageDto dto) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Image not found"));

        image.setUrl(dto.getUrl());
        image.setAltText(dto.getAltText());

        if (!image.getProduct().getId().equals(dto.getProductId())) {
            Product newProduct = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));
            image.setProduct(newProduct);
        }

        Image updated = imageRepository.save(image);
        return toDto(updated);
    }

    public void deleteImage(Integer id) {
        if (!imageRepository.existsById(id)) {
            throw new EntityNotFoundException("Image not found");
        }
        imageRepository.deleteById(id);
    }

    private ImageDto toDto(Image image) {
        return new ImageDto(
                image.getId().intValue(),
                image.getUrl(),
                image.getAltText(),
                image.getProduct().getId().intValue()
        );
    }
}
