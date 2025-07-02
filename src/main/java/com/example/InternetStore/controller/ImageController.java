package com.example.InternetStore.controller;

import com.example.InternetStore.dto.ImageDto;
import com.example.InternetStore.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    // Отримати всі зображення для продукту

    @GetMapping("/all/{productId}")
    public ResponseEntity<List<ImageDto>> getImagesByProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(imageService.getImagesByProductId(productId));
    }



    // Створити зображення

    @PostMapping
    public ResponseEntity<ImageDto> createImage(@RequestBody ImageDto imageDto) {
        return ResponseEntity.ok(imageService.createImage(imageDto));
    }

    // Оновити зображення

    @PutMapping("/update/{id}")
    public ResponseEntity<ImageDto> updateImage(@PathVariable Integer id, @RequestBody ImageDto imageDto) {
        return ResponseEntity.ok(imageService.updateImage(id, imageDto));
    }

    // Видалити зображення

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Integer id) {
        imageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<ImageDto>> getAllImages() {
        return ResponseEntity.ok(imageService.getAllImages());
    }
}
