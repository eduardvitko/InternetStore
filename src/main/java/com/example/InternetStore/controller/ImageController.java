package com.example.InternetStore.controller;

import com.example.InternetStore.dto.ImageDto;
import com.example.InternetStore.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // <-- ВАЖЛИВИЙ ІМПОРТ
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

    // --- ПУБЛІЧНИЙ ЕНДПОІНТ ---
    /**
     * Отримує всі зображення для конкретного продукту.
     * Цей ендпоінт має бути доступний для всіх, щоб бачити картинки товарів.
     */
    @GetMapping("/all/{productId}")
    public ResponseEntity<List<ImageDto>> getImagesByProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(imageService.getImagesByProductId(productId));
    }


    // --- АДМІНСЬКІ ЕНДПОІНТИ ---

    /**
     * Створює нове зображення. Доступно тільки для ADMIN.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // <-- ДОДАНО ЗАХИСТ
    public ResponseEntity<ImageDto> createImage(@RequestBody ImageDto imageDto) {
        return ResponseEntity.ok(imageService.createImage(imageDto));
    }

    /**
     * Оновлює зображення. Доступно тільки для ADMIN.
     */
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')") // <-- ДОДАНО ЗАХИСТ
    public ResponseEntity<ImageDto> updateImage(@PathVariable Integer id, @RequestBody ImageDto imageDto) {
        return ResponseEntity.ok(imageService.updateImage(id, imageDto));
    }

    /**
     * Видаляє зображення. Доступно тільки для ADMIN.
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')") // <-- ДОДАНО ЗАХИСТ
    public ResponseEntity<Void> deleteImage(@PathVariable Integer id) {
        imageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Отримує ВСІ зображення в системі. Доступно тільки для ADMIN.
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')") // <-- ДОДАНО ЗАХИСТ
    public ResponseEntity<List<ImageDto>> getAllImages() {
        return ResponseEntity.ok(imageService.getAllImages());
    }
}