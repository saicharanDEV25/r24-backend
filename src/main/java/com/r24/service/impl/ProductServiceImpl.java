package com.r24.service.impl;

import com.r24.entity.Product;
import com.r24.repository.FavoriteRepository;
import com.r24.repository.ProductRepository;
import com.r24.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final FavoriteRepository favoriteRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                               FavoriteRepository favoriteRepository) {
        this.productRepository = productRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAllWithCategory();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product updateProduct(Long id, Product product) {

        Product existing = productRepository.findById(id).orElse(null);

        if (existing == null) {
            return null;
        }

        existing.setName(product.getName());
        existing.setBrand(product.getBrand());
        existing.setModel(product.getModel());
        existing.setCategory(product.getCategory());
        existing.setPrice(product.getPrice());
        existing.setDescription(product.getDescription());
        existing.setImageUrl(product.getImageUrl());
        existing.setStock(product.getStock());
        existing.setActive(product.getActive());

        return productRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        // A product still favorited by a customer can't be deleted directly
        // — favorites.product_id is a non-null FK with no cascade, so the
        // delete would fail with a raw DB constraint violation. Clear those
        // favorites first; the product is gone from the catalog either way.
        favoriteRepository.deleteByProductId(id);
        productRepository.deleteById(id);
    }
}