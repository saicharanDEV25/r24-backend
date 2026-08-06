package com.r24.controller;

import com.r24.entity.Customer;
import com.r24.entity.Favorite;
import com.r24.entity.Product;
import com.r24.repository.CustomerRepository;
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
@CrossOrigin(origins = "*")
public class FavoriteController {

    private final FavoriteRepository favoriteRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CustomerAuthHelper authHelper;

    public FavoriteController(FavoriteRepository favoriteRepository,
                               CustomerRepository customerRepository,
                               ProductRepository productRepository,
                               CustomerAuthHelper authHelper) {
        this.favoriteRepository = favoriteRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.authHelper = authHelper;
    }

    private Customer currentCustomer(HttpServletRequest request) {
        String phone = authHelper.resolvePhoneNumber(request);
        return customerRepository.findByPhoneNumber(phone)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @GetMapping
    public List<Product> getMyFavorites(HttpServletRequest request) {

        Customer customer = currentCustomer(request);

        return favoriteRepository.findByCustomerId(customer.getId())
                .stream()
                .map(Favorite::getProduct)
                .collect(Collectors.toList());
    }

    @PostMapping("/{productId}")
    public String addFavorite(HttpServletRequest request, @PathVariable Long productId) {

        Customer customer = currentCustomer(request);

        boolean alreadySaved = favoriteRepository
                .findByCustomerIdAndProductId(customer.getId(), productId)
                .isPresent();

        if (alreadySaved) {
            return "Already in favorites";
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

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

        Customer customer = currentCustomer(request);

        favoriteRepository.findByCustomerIdAndProductId(customer.getId(), productId)
                .ifPresent(favoriteRepository::delete);

        return "Removed from favorites";
    }
}
