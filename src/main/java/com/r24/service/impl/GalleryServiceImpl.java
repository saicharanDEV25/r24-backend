package com.r24.service.impl;

import com.r24.entity.Gallery;
import com.r24.repository.GalleryRepository;
import com.r24.service.GalleryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GalleryServiceImpl implements GalleryService {

    private final GalleryRepository galleryRepository;

    public GalleryServiceImpl(GalleryRepository galleryRepository) {
        this.galleryRepository = galleryRepository;
    }

    @Override
    public Gallery addGallery(Gallery gallery) {
        return galleryRepository.save(gallery);
    }

    @Override
    public List<Gallery> getAllGallery() {
        return galleryRepository.findAll();
    }

    @Override
    public Gallery getGalleryById(Long id) {
        return galleryRepository.findById(id).orElse(null);
    }

    @Override
    public Gallery updateGallery(Long id, Gallery gallery) {

        Gallery existing = galleryRepository.findById(id).orElse(null);

        if (existing == null) {
            return null;
        }

        existing.setTitle(gallery.getTitle());
        existing.setBeforeImageUrl(gallery.getBeforeImageUrl());
        existing.setAfterImageUrl(gallery.getAfterImageUrl());
        existing.setDescription(gallery.getDescription());
        existing.setActive(gallery.getActive());

        return galleryRepository.save(existing);
    }

    @Override
    public void deleteGallery(Long id) {
        galleryRepository.deleteById(id);
    }

}