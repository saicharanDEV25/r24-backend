package com.r24.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // Free-text bike brand ("KTM", "Royal Enfield", "Yamaha", "Benelli" —
    // matches BIKE_BRANDS on the frontend). Not a FK: categories (Air
    // Filters, Chain & Sprockets, etc.) are brand-agnostic part types
    // shared across every brand, so brand lives on the product itself.
    private String brand;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    private Double price;

    @Column(length = 1000)
    private String description;

    private String imageUrl;

    private Integer stock;

    private Boolean active = true;
}