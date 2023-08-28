package com.fon.service;

import com.fon.DAO.ImageRepository;
import com.fon.entity.Image;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class ImageService {

    private final ImageRepository imageRepository;


    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image save(Image image) {
        return imageRepository.save(image);
    }

    public void deleteAll (List<Image> images) {
        imageRepository.deleteAll(images);
    }

}
