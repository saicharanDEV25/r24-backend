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
 * Seeds a realistic KTM genuine-parts and riding-gear catalog on first run.
 * Each block is guarded by its own marker category so it never re-runs or
 * duplicates rows once seeded, and never touches categories/products an
 * admin has already created.
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
        List<Category> existing = categoryRepository.findAll();

        boolean partsSeeded = existing.stream()
                .anyMatch(c -> "Air Filters".equalsIgnoreCase(c.getName()));

        if (!partsSeeded) {
            seedGenuineParts();
        }

        boolean gearSeeded = existing.stream()
                .anyMatch(c -> "Exhaust Systems".equalsIgnoreCase(c.getName()));

        if (!gearSeeded) {
            seedRidingGear();
        }
    }

    private void seedGenuineParts() {
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

    private void seedRidingGear() {
        Category exhausts = saveCategory("Exhaust Systems", "Slip-on and full exhaust systems for a deeper note and a few extra horses.");
        Category helmets = saveCategory("Helmets", "Full-face, flip-up and off-road helmets for every kind of ride.");
        Category ridingGear = saveCategory("Riding Gear", "Jackets, gloves, pants and boots for real protection on the road.");

        productRepository.saveAll(List.of(
                product("Stock-Replacement Exhaust Muffler", exhausts,
                        "OEM-spec replacement muffler for a clean, quiet, factory-matched exhaust note.",
                        7500.0, 10, false),
                product("Stainless Steel Slip-On Exhaust", exhausts,
                        "Bolt-on stainless steel slip-on for a deeper tone and a mild weight saving over stock.",
                        11500.0, 8, true),
                product("Akrapovič Slip-On Exhaust — Duke/RC Series", exhausts,
                        "Akrapovič slip-on with a titanium muffler, sharper throttle response and a signature growl.",
                        28500.0, 3, true),
                product("SC-Project Full System Exhaust", exhausts,
                        "Complete SC-Project system replacing header and muffler for maximum flow and weight savings.",
                        34900.0, 0, false),
                product("Full Titanium Race Exhaust System", exhausts,
                        "Track-focused full titanium system — lightest option, loudest note, race use only.",
                        42000.0, 2, false),

                product("KTM PowerWear Full-Face Racing Helmet", helmets,
                        "KTM-branded full-face helmet with aerodynamic shell and multi-point ventilation.",
                        13500.0, 12, true),
                product("AGV K1 Full-Face Helmet", helmets,
                        "AGV K1 with a race-derived shell, pinlock-ready visor and plush removable liner.",
                        16800.0, 6, false),
                product("Studds Ninja Half-Face Helmet", helmets,
                        "Lightweight, ISI-certified half-face helmet for everyday city commuting.",
                        1650.0, 30, false),
                product("Steelbird Modular Flip-Up Helmet", helmets,
                        "Flip-up modular helmet — full-face protection with half-face convenience at a stop.",
                        4200.0, 18, false),
                product("KTM Off-Road Motocross Helmet", helmets,
                        "Motocross-style helmet with extended chin bar and peak, built for off-road riding.",
                        7900.0, 7, false),

                product("KTM PowerWear All-Season Riding Jacket", ridingGear,
                        "All-season riding jacket with removable thermal liner and CE-rated shoulder/elbow armor.",
                        9500.0, 10, true),
                product("Mesh Riding Jacket (Summer)", ridingGear,
                        "Ventilated mesh jacket built for hot-weather riding without giving up armor protection.",
                        5200.0, 15, false),
                product("Riding Gloves — Knuckle Protection", ridingGear,
                        "Knuckle-armored riding gloves with a reinforced palm for grip and abrasion resistance.",
                        1800.0, 25, false),
                product("Riding Pants with Knee Armor", ridingGear,
                        "Reinforced riding pants with CE-rated knee armor and adjustable fit.",
                        6500.0, 9, false),
                product("Riding Boots — Ankle Protection", ridingGear,
                        "Ankle-protected riding boots with a non-slip sole, built for daily commuting and touring.",
                        7200.0, 8, false)
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
