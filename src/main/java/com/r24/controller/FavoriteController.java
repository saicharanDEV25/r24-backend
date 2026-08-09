package com.r24.controller;

import com.r24.entity.Customer;
import com.r24.entity.Favorite;
import com.r24.entity.Product;
import com.r24.exception.ResourceNotFoundException;
import com.r24.repository.FavoriteRepository;
import com.r24.repository.ProductRepository;
import com.r24.security.CustomerAuthHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;
    private final CustomerAuthHelper authHelper;

    public FavoriteController(FavoriteRepository favoriteRepository,
                               ProductRepository productRepository,
                               CustomerAuthHelper authHelper) {
        this.favoriteRepository = favoriteRepository;
        this.productRepository = productRepository;
        this.authHelper = authHelper;
    }

    @GetMapping
    public List<Product> getMyFavorites(HttpServletRequest request) {

        Customer customer = authHelper.resolveCustomer(request);

        return favoriteRepository.findByCustomerId(customer.getId())
                .stream()
                .map(Favorite::getProduct)
                .collect(Collectors.toList());
    }

    @PostMapping("/{productId}")
    public String addFavorite(HttpServletRequest request, @PathVariable Long productId) {

        Customer customer = authHelper.resolveCustomer(request);

        boolean alreadySaved = favoriteRepository
                .findByCustomerIdAndProductId(customer.getId(), productId)
                .isPresent();

        if (alreadySaved) {
            return "Already in favorites";
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        favoriteRepository.save(
                Favorite.builder()
                        .customer(customer)
                        .product(product)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        return "Added to favorites";
    }

    @DeleteMapping("/{productId}")
    public String removeFavorite(HttpServletRequest request, @PathVariable Long productId) {

        Customer customer = authHelper.resolveCustomer(request);

        favoriteRepository.findByCustomerIdAndProductId(customer.getId(), productId)
                .ifPresent(favoriteRepository::delete);

        return "Removed from favorites";
    }
}
