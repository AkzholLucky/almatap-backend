package com.almatap.AlmatapBackend.services;

import com.almatap.AlmatapBackend.models.Image;
import com.almatap.AlmatapBackend.repositories.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ImageService {
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Transactional
    public void addImage(Image image){
        imageRepository.save(image);
    }

    public Image findById(int id){
        return imageRepository.findById(id).orElse(null);
    }

    public List<Image> findAll(){
        return imageRepository.findAll();
    }
}
