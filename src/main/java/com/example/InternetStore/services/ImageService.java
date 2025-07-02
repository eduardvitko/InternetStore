package com.example.InternetStore.services;

import com.example.InternetStore.dto.ImageDto;
import com.example.InternetStore.model.Image;
import com.example.InternetStore.model.Product;
import com.example.InternetStore.reposietories.ImageRepository;
import com.example.InternetStore.reposietories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;

    public ImageService(ImageRepository imageRepository, ProductRepository productRepository) {
        this.imageRepository = imageRepository;
        this.productRepository = productRepository;
    }

    public ImageDto createImage(ImageDto dto) {
        if (dto.getProductId() == null) {
            throw new IllegalArgumentException("ProductId must not be null");
        }
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Image image = new Image(dto.getUrl(), dto.getAltText(), product);
        Image saved = imageRepository.save(image);

        return toDto(saved);
    }

    public List<ImageDto> getImagesByProductId(Integer productId) {
        return imageRepository.findByProductId(productId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<ImageDto> getImageById(Integer id) {
        return imageRepository.findById(id).map(this::toDto);
    }

    public ImageDto updateImage(Integer id, ImageDto dto) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        image.setUrl(dto.getUrl());
        image.setAltText(dto.getAltText());

        if (dto.getProductId() == null) {
            throw new IllegalArgumentException("ProductId не може бути null");
        }

        if (!image.getProduct().getId().equals(dto.getProductId())) {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            image.setProduct(product);
        }

        return toDto(imageRepository.save(image));
    }

    public void deleteImage(Integer id) {
        if (!imageRepository.existsById(id)) {
            throw new RuntimeException("Image with id " + id + " not found");
        }
        imageRepository.deleteById(id);
    }

    private ImageDto toDto(Image image) {
        return new ImageDto(
                image.getId(),
                image.getUrl(),
                image.getAltText(),
                image.getProduct().getId()
        );
    }

    public List<ImageDto> getAllImages() {
        List<Image> images = imageRepository.findAll();
        return images.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
