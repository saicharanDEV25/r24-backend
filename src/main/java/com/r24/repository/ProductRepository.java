package com.r24.repository;

import com.r24.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findTop5ByOrderByIdDesc();

    // Category is @ManyToOne (EAGER by default) with open-in-view enabled,
    // so a plain findAll() triggers a separate query per distinct category
    // while Jackson serializes the response. One JOIN FETCH avoids that.
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category")
    List<Product> findAllWithCategory();
}