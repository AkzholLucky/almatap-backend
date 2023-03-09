package com.almatap.AlmatapBackend.controllers;

import com.almatap.AlmatapBackend.models.Image;
import com.almatap.AlmatapBackend.services.ImageService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/images/{id}")
    private ResponseEntity<?> getImageById(@PathVariable int id) {
        Image image = imageService.findById(id);
        return ResponseEntity.ok()
                .body(new InputStreamResource(new ByteArrayInputStream(image.getImage())));
    }
}
