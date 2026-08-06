package com.r24.config;

import com.r24.entity.Category;
import com.r24.entity.Product;
import com.r24.repository.CategoryRepository;
import com.r24.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Seeds a realistic KTM genuine-parts catalog on first run. Guarded by a
 * marker category so it never re-runs or duplicates rows once seeded, and
 * never touches categories/products an admin has already created.
 */
@Component
@RequiredArgsConstructor
public class KtmPartsDataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    private static final String PLACEHOLDER_BASE =
            "https://placehold.co/500x500/1a1a1a/FFD000?text=";

    @Override
    public void run(String... args) {
        boolean alreadySeeded = categoryRepository.findAll().stream()
                .anyMatch(c -> "Air Filters".equalsIgnoreCase(c.getName()));

        if (alreadySeeded) {
            return;
        }

        Category airFilters = saveCategory("Air Filters", "Genuine and performance air filters for KTM Duke & RC models.");
        Category oilFilters = saveCategory("Oil Filters", "Genuine engine oil filters for reliable filtration and engine life.");
        Category chainSprockets = saveCategory("Chain & Sprockets", "Chain and sprocket kits for smooth, durable power transmission.");
        Category brakePads = saveCategory("Brake Pads", "Front and rear brake pads for confident, consistent stopping power.");
        Category clutchPlates = saveCategory("Clutch Plates", "Clutch plate kits for smooth gear shifts and reliable power delivery.");
        Category fuelPumps = saveCategory("Fuel Pumps", "Fuel pump assemblies for consistent fuel delivery and performance.");
        Category batteries = saveCategory("Batteries", "Maintenance-free batteries for dependable starts every time.");

        productRepository.saveAll(List.of(
                product("KTM Duke 200 Air Filter", airFilters,
                        "Genuine replacement air filter for KTM Duke 200, engineered for optimal airflow and engine protection.",
                        850.0, 12, true),
                product("KTM RC 390 High-Flow Air Filter", airFilters,
                        "High-flow performance air filter for KTM RC 390, improves throttle response and airflow.",
                        1450.0, 0, false),

                product("KTM Genuine Oil Filter (Duke/RC Series)", oilFilters,
                        "OEM-spec oil filter compatible across the KTM Duke and RC series for cleaner engine oil circulation.",
                        350.0, 40, false),

                product("KTM 390 Chain & Sprocket Kit", chainSprockets,
                        "Complete chain and sprocket kit for KTM 390 models, built for durability and smooth power transfer.",
                        4200.0, 8, true),
                product("KTM 200 Duke Sprocket Set", chainSprockets,
                        "Front and rear sprocket set for KTM 200 Duke, precision-machined for a perfect chain fit.",
                        2100.0, 15, false),

                product("KTM Front Brake Pad Set", brakePads,
                        "High-friction front brake pad set for consistent bite and reduced stopping distance.",
                        1200.0, 20, false),
                product("KTM Rear Brake Pad Set", brakePads,
                        "Durable rear brake pad set engineered for KTM Duke and RC models.",
                        950.0, 18, false),

                product("KTM Duke 390 Clutch Plate Kit", clutchPlates,
                        "Complete clutch plate kit for KTM Duke 390, restores smooth engagement and shift feel.",
                        3200.0, 6, false),

                product("KTM RC 200 Fuel Pump Assembly", fuelPumps,
                        "OEM-spec fuel pump assembly for KTM RC 200, ensures consistent fuel pressure and delivery.",
                        5800.0, 0, false),

                product("KTM Duke/RC Series Battery (12V)", batteries,
                        "Maintenance-free 12V battery sized for the KTM Duke and RC series, reliable cold starts.",
                        2800.0, 10, false)
        ));
    }

    private Category saveCategory(String name, String description) {
        Category category = Category.builder()
                .name(name)
                .description(description)
                .imageUrl(PLACEHOLDER_BASE + name.replace(" ", "+").replace("&", "and"))
                .active(true)
                .build();

        return categoryRepository.save(category);
    }

    private Product product(String name, Category category, String description,
                             Double price, Integer stock, Boolean featured) {
        return Product.builder()
                .name(name)
                .category(category)
                .price(price)
                .description(description)
                .imageUrl(PLACEHOLDER_BASE + category.getName().replace(" ", "+").replace("&", "and"))
                .stock(stock)
                .featured(featured)
                .active(true)
                .build();
    }
}
