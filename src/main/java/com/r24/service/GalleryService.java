package com.r24.service;

import com.r24.entity.Gallery;

import java.util.List;

public interface GalleryService {

    Gallery addGallery(Gallery gallery);

    List<Gallery> getAllGallery();

    Gallery getGalleryById(Long id);

    Gallery updateGallery(Long id, Gallery gallery);

    void deleteGallery(Long id);
}