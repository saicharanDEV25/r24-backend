package com.r24.controller;

import com.r24.entity.Gallery;
import com.r24.service.GalleryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gallery")
@CrossOrigin(origins = "*")
public class GalleryController {

    private final GalleryService galleryService;

    public GalleryController(GalleryService galleryService) {
        this.galleryService = galleryService;
    }

    @PostMapping
    public Gallery addGallery(@RequestBody Gallery gallery) {
        return galleryService.addGallery(gallery);
    }

    @GetMapping
    public List<Gallery> getAllGallery() {
        return galleryService.getAllGallery();
    }

    @GetMapping("/{id}")
    public Gallery getGalleryById(@PathVariable Long id) {
        return galleryService.getGalleryById(id);
    }

    @PutMapping("/{id}")
    public Gallery updateGallery(@PathVariable Long id,
                                 @RequestBody Gallery gallery) {
        return galleryService.updateGallery(id, gallery);
    }

    @DeleteMapping("/{id}")
    public String deleteGallery(@PathVariable Long id) {
        galleryService.deleteGallery(id);
        return "Gallery Deleted Successfully";
    }
}