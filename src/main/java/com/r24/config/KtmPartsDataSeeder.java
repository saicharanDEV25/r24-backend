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
 * Seeds the KTM parts/gear catalog on first run. Each block is guarded by
 * its own marker category so it never re-seeds or touches admin-created data.
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

        boolean expandedPartsSeeded = existing.stream()
                .anyMatch(c -> "Spark Plugs".equalsIgnoreCase(c.getName()));

        if (!expandedPartsSeeded) {
            seedExpandedKtmAccessories();
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
                        850.0, 12),
                product("KTM RC 390 High-Flow Air Filter", airFilters,
                        "High-flow performance air filter for KTM RC 390, improves throttle response and airflow.",
                        1450.0, 0),

                product("KTM Genuine Oil Filter (Duke/RC Series)", oilFilters,
                        "OEM-spec oil filter compatible across the KTM Duke and RC series for cleaner engine oil circulation.",
                        350.0, 40),

                product("KTM 390 Chain & Sprocket Kit", chainSprockets,
                        "Complete chain and sprocket kit for KTM 390 models, built for durability and smooth power transfer.",
                        4200.0, 8),
                product("KTM 200 Duke Sprocket Set", chainSprockets,
                        "Front and rear sprocket set for KTM 200 Duke, precision-machined for a perfect chain fit.",
                        2100.0, 15),

                product("KTM Front Brake Pad Set", brakePads,
                        "High-friction front brake pad set for consistent bite and reduced stopping distance.",
                        1200.0, 20),
                product("KTM Rear Brake Pad Set", brakePads,
                        "Durable rear brake pad set engineered for KTM Duke and RC models.",
                        950.0, 18),

                product("KTM Duke 390 Clutch Plate Kit", clutchPlates,
                        "Complete clutch plate kit for KTM Duke 390, restores smooth engagement and shift feel.",
                        3200.0, 6),

                product("KTM RC 200 Fuel Pump Assembly", fuelPumps,
                        "OEM-spec fuel pump assembly for KTM RC 200, ensures consistent fuel pressure and delivery.",
                        5800.0, 0),

                product("KTM Duke/RC Series Battery (12V)", batteries,
                        "Maintenance-free 12V battery sized for the KTM Duke and RC series, reliable cold starts.",
                        2800.0, 10)
        ));
    }

    private void seedRidingGear() {
        Category exhausts = saveCategory("Exhaust Systems", "Slip-on and full exhaust systems for a deeper note and a few extra horses.");
        Category helmets = saveCategory("Helmets", "Full-face, flip-up and off-road helmets for every kind of ride.");
        Category ridingGear = saveCategory("Riding Gear", "Jackets, gloves, pants and boots for real protection on the road.");

        productRepository.saveAll(List.of(
                product("Stock-Replacement Exhaust Muffler", exhausts,
                        "OEM-spec replacement muffler for a clean, quiet, factory-matched exhaust note.",
                        7500.0, 10),
                product("Stainless Steel Slip-On Exhaust", exhausts,
                        "Bolt-on stainless steel slip-on for a deeper tone and a mild weight saving over stock.",
                        11500.0, 8),
                product("Akrapovič Slip-On Exhaust — Duke/RC Series", exhausts,
                        "Akrapovič slip-on with a titanium muffler, sharper throttle response and a signature growl.",
                        28500.0, 3),
                product("SC-Project Full System Exhaust", exhausts,
                        "Complete SC-Project system replacing header and muffler for maximum flow and weight savings.",
                        34900.0, 0),
                product("Full Titanium Race Exhaust System", exhausts,
                        "Track-focused full titanium system — lightest option, loudest note, race use only.",
                        42000.0, 2),

                product("KTM PowerWear Full-Face Racing Helmet", helmets,
                        "KTM-branded full-face helmet with aerodynamic shell and multi-point ventilation.",
                        13500.0, 12),
                product("AGV K1 Full-Face Helmet", helmets,
                        "AGV K1 with a race-derived shell, pinlock-ready visor and plush removable liner.",
                        16800.0, 6),
                product("Studds Ninja Half-Face Helmet", helmets,
                        "Lightweight, ISI-certified half-face helmet for everyday city commuting.",
                        1650.0, 30),
                product("Steelbird Modular Flip-Up Helmet", helmets,
                        "Flip-up modular helmet — full-face protection with half-face convenience at a stop.",
                        4200.0, 18),
                product("KTM Off-Road Motocross Helmet", helmets,
                        "Motocross-style helmet with extended chin bar and peak, built for off-road riding.",
                        7900.0, 7),

                product("KTM PowerWear All-Season Riding Jacket", ridingGear,
                        "All-season riding jacket with removable thermal liner and CE-rated shoulder/elbow armor.",
                        9500.0, 10),
                product("Mesh Riding Jacket (Summer)", ridingGear,
                        "Ventilated mesh jacket built for hot-weather riding without giving up armor protection.",
                        5200.0, 15),
                product("Riding Gloves — Knuckle Protection", ridingGear,
                        "Knuckle-armored riding gloves with a reinforced palm for grip and abrasion resistance.",
                        1800.0, 25),
                product("Riding Pants with Knee Armor", ridingGear,
                        "Reinforced riding pants with CE-rated knee armor and adjustable fit.",
                        6500.0, 9),
                product("Riding Boots — Ankle Protection", ridingGear,
                        "Ankle-protected riding boots with a non-slip sole, built for daily commuting and touring.",
                        7200.0, 8)
        ));
    }

    // Expands the catalog into existing categories plus new ones, covering every KTM model family.
    private void seedExpandedKtmAccessories() {
        Category airFilters = getOrCreateCategory("Air Filters", "Genuine and performance air filters for KTM Duke & RC models.");
        Category oilFilters = getOrCreateCategory("Oil Filters", "Genuine engine oil filters for reliable filtration and engine life.");
        Category chainSprockets = getOrCreateCategory("Chain & Sprockets", "Chain and sprocket kits for smooth, durable power transmission.");
        Category brakePads = getOrCreateCategory("Brake Pads", "Front and rear brake pads for confident, consistent stopping power.");
        Category clutchPlates = getOrCreateCategory("Clutch Plates", "Clutch plate kits for smooth gear shifts and reliable power delivery.");
        Category fuelPumps = getOrCreateCategory("Fuel Pumps", "Fuel pump assemblies for consistent fuel delivery and performance.");
        Category batteries = getOrCreateCategory("Batteries", "Maintenance-free batteries for dependable starts every time.");

        Category sparkPlugs = saveCategory("Spark Plugs", "Genuine and iridium performance spark plugs for a cleaner burn and easier starts.");
        Category brakeDiscs = saveCategory("Brake Discs & Rotors", "Front and rear brake discs machined for consistent, fade-free braking.");
        Category grips = saveCategory("Handlebar Grips & Levers", "Grips, clutch and brake levers for control and everyday comfort.");
        Category mirrors = saveCategory("Mirrors", "OEM-style, bar-end and folding mirrors for every riding style.");
        Category footPegs = saveCategory("Foot Pegs & Rearsets", "Rider and passenger foot pegs, rearset kits and grip upgrades.");
        Category suspension = saveCategory("Suspension & Fork Parts", "Fork seals, springs, oil and rear shocks for a dialled-in ride.");
        Category cooling = saveCategory("Radiator & Cooling", "Radiator guards, coolant and cooling system parts.");
        Category cablesWiring = saveCategory("Cables & Wiring", "Clutch, throttle and speedometer cables plus wiring essentials.");
        Category lighting = saveCategory("Lighting & Bulbs", "Headlights, indicators, tail lights and replacement bulbs.");
        Category seating = saveCategory("Seat & Comfort", "Seats, seat covers and comfort upgrades for longer rides.");
        Category crashGuards = saveCategory("Crash Guards & Frame Sliders", "Engine guards and frame sliders that keep a tip-over cheap.");
        Category tailTidy = saveCategory("Number Plate & Tail Tidy", "Fender eliminators, plate brackets and splash guards.");

        productRepository.saveAll(List.of(
                // ---------- Air Filters ----------
                product("KTM 125 Duke Air Filter", airFilters,
                        "Genuine-spec replacement air filter for the 125 Duke, restores clean airflow and engine protection.",
                        750.0, 14),
                product("KTM 250 Duke Air Filter", airFilters,
                        "Genuine-spec replacement air filter for the 250 Duke.",
                        820.0, 12),
                product("KTM 390 Duke Air Filter", airFilters,
                        "Genuine-spec replacement air filter for the 390 Duke.",
                        890.0, 15),
                product("KTM RC 200 Air Filter", airFilters,
                        "Genuine-spec replacement air filter for the RC 200.",
                        870.0, 10),
                product("KTM 390 Adventure Air Filter", airFilters,
                        "Genuine-spec replacement air filter for the 390 Adventure and Adventure X.",
                        950.0, 9),
                product("KTM 790/890 Duke Air Filter", airFilters,
                        "High-flow air filter sized for the 790 Duke and 890 Duke R twins.",
                        1650.0, 6),
                product("KTM 1290 Super Duke R Air Filter", airFilters,
                        "High-flow air filter for the 1290 Super Duke R, engineered for maximum breathing.",
                        2200.0, 4),

                // ---------- Oil Filters ----------
                product("KTM 790/890 Duke Oil Filter", oilFilters,
                        "OEM-spec oil filter sized for the 790 Duke and 890 Duke R.",
                        420.0, 20),
                product("KTM 1290 Super Duke R Oil Filter", oilFilters,
                        "OEM-spec oil filter for the 1290 Super Duke R.",
                        480.0, 15),
                product("KTM 390 Adventure Oil Filter", oilFilters,
                        "OEM-spec oil filter for the 390 Adventure series.",
                        360.0, 22),
                product("Racing High-Flow Oil Filter (Universal)", oilFilters,
                        "Performance oil filter with a higher flow rate for spirited and track riding.",
                        650.0, 10),

                // ---------- Spark Plugs ----------
                product("KTM 125-250 Duke Spark Plug", sparkPlugs,
                        "Genuine-spec spark plug for the 125, 200 and 250 Duke.",
                        280.0, 30),
                product("KTM 390 Series Spark Plug", sparkPlugs,
                        "Genuine-spec spark plug for the 390 Duke, RC 390 and 390 Adventure.",
                        320.0, 28),
                product("KTM RC Series Spark Plug", sparkPlugs,
                        "Genuine-spec spark plug for the RC 200 and RC 390.",
                        320.0, 25),
                product("KTM 790/890 Duke Spark Plug (Set of 2)", sparkPlugs,
                        "Genuine-spec spark plug pair for the 790 Duke and 890 Duke R twins.",
                        680.0, 12),
                product("KTM 1290 Super Duke R Spark Plug (Set of 2)", sparkPlugs,
                        "Genuine-spec spark plug pair for the 1290 Super Duke R.",
                        780.0, 8),
                product("Iridium Performance Spark Plug (Universal)", sparkPlugs,
                        "Iridium-tipped upgrade plug for a cleaner burn and easier cold starts.",
                        950.0, 10),

                // ---------- Chain & Sprockets ----------
                product("KTM 125 Duke Chain & Sprocket Kit", chainSprockets,
                        "Complete chain and sprocket kit for the 125 Duke.",
                        3600.0, 7),
                product("KTM RC 390 Chain & Sprocket Kit", chainSprockets,
                        "Complete chain and sprocket kit for the RC 390.",
                        4400.0, 6),
                product("KTM 390 Adventure Chain & Sprocket Kit", chainSprockets,
                        "Complete chain and sprocket kit for the 390 Adventure series.",
                        4600.0, 5),
                product("KTM 790 Duke Chain & Sprocket Kit", chainSprockets,
                        "Complete chain and sprocket kit sized for the 790 Duke.",
                        6200.0, 4),
                product("O-Ring Heavy-Duty Chain (Universal Upgrade)", chainSprockets,
                        "Sealed O-ring chain upgrade for longer life and less maintenance.",
                        3800.0, 8),

                // ---------- Brake Pads ----------
                product("KTM 390 Series Front Brake Pad Set", brakePads,
                        "Front brake pad set for the 390 Duke, RC 390 and 390 Adventure.",
                        1350.0, 16),
                product("KTM 390 Series Rear Brake Pad Set", brakePads,
                        "Rear brake pad set for the 390 Duke, RC 390 and 390 Adventure.",
                        1050.0, 16),
                product("KTM RC Series Sintered Front Brake Pad Set", brakePads,
                        "Sintered front brake pad set for stronger bite on the RC 200 and RC 390.",
                        1550.0, 10),
                product("KTM 790/890 Duke Front Brake Pad Set", brakePads,
                        "Front brake pad set for the 790 Duke and 890 Duke R.",
                        1950.0, 8),
                product("KTM 1290 Super Duke R Front Brake Pad Set (Radial)", brakePads,
                        "Radial-caliper front brake pad set for the 1290 Super Duke R.",
                        2600.0, 5),
                product("KTM Adventure Series Brake Pad Set (Front + Rear)", brakePads,
                        "Front and rear brake pad set for the 390 Adventure series.",
                        2100.0, 9),

                // ---------- Brake Discs & Rotors ----------
                product("KTM 200-390 Duke Front Brake Disc", brakeDiscs,
                        "Precision-machined front brake disc for the 200, 250 and 390 Duke.",
                        2800.0, 6),
                product("KTM RC Series Front Brake Disc", brakeDiscs,
                        "Precision-machined front brake disc for the RC 200 and RC 390.",
                        3100.0, 6),
                product("KTM 390 Adventure Front Brake Disc", brakeDiscs,
                        "Precision-machined front brake disc for the 390 Adventure series.",
                        3200.0, 5),
                product("KTM 790/890 Duke Front Brake Disc (Dual)", brakeDiscs,
                        "Dual front brake disc set for the 790 Duke and 890 Duke R.",
                        5600.0, 3),
                product("KTM 1290 Super Duke R Front Brake Disc (Dual)", brakeDiscs,
                        "Dual front brake disc set for the 1290 Super Duke R.",
                        7200.0, 2),
                product("Universal Rear Brake Disc", brakeDiscs,
                        "Rear brake disc compatible across most KTM Duke, RC and Adventure models.",
                        1900.0, 8),

                // ---------- Clutch Plates ----------
                product("KTM 200 Duke Clutch Plate Kit", clutchPlates,
                        "Complete clutch plate kit for the 200 Duke.",
                        2800.0, 7),
                product("KTM RC 390 Clutch Plate Kit", clutchPlates,
                        "Complete clutch plate kit for the RC 390.",
                        3200.0, 6),
                product("KTM 790 Duke Clutch Plate Kit (Slip-Assist Compatible)", clutchPlates,
                        "Clutch plate kit for the 790 Duke, compatible with the slip-assist clutch.",
                        5400.0, 4),
                product("KTM 1290 Super Duke R Clutch Plate Kit", clutchPlates,
                        "Complete clutch plate kit for the 1290 Super Duke R.",
                        6800.0, 3),

                // ---------- Fuel Pumps ----------
                product("KTM 390 Series Fuel Pump Assembly", fuelPumps,
                        "OEM-spec fuel pump assembly for the 390 Duke and RC 390.",
                        6200.0, 4),
                product("KTM Adventure Series Fuel Pump Assembly", fuelPumps,
                        "OEM-spec fuel pump assembly for the 390 Adventure series.",
                        6500.0, 3),
                product("KTM 790/890 Duke Fuel Pump Assembly", fuelPumps,
                        "OEM-spec fuel pump assembly for the 790 Duke and 890 Duke R.",
                        8900.0, 2),

                // ---------- Batteries ----------
                product("KTM 125-250 Duke Battery (12V, Compact)", batteries,
                        "Compact maintenance-free 12V battery sized for the smaller Duke models.",
                        2400.0, 12),
                product("KTM 790/890 Duke Battery (12V, High-CCA)", batteries,
                        "Higher cold-cranking-amp 12V battery for the 790 Duke and 890 Duke R.",
                        3600.0, 6),
                product("KTM 1290 Super Duke R Battery (12V, Lithium-Ion)", batteries,
                        "Lightweight lithium-ion battery upgrade for the 1290 Super Duke R.",
                        6800.0, 3),

                // ---------- Handlebar Grips & Levers ----------
                product("KTM Ergonomic Rubber Grip Set", grips,
                        "Vibration-damping rubber grip set for all-day riding comfort.",
                        650.0, 20),
                product("KTM Racing Silicone Grip Set", grips,
                        "Tacky silicone grip set for a firmer, sportier hold.",
                        850.0, 15),
                product("Adjustable Clutch Lever (Universal)", grips,
                        "Reach-adjustable clutch lever compatible across the KTM Duke/RC range.",
                        1450.0, 10),
                product("Adjustable Brake Lever (Universal)", grips,
                        "Reach-adjustable brake lever compatible across the KTM Duke/RC range.",
                        1450.0, 10),
                product("Folding & Extendable Lever Set", grips,
                        "Crash-friendly folding, extendable clutch and brake lever set.",
                        2600.0, 6),
                product("Bar-End Weights (Vibration Damping)", grips,
                        "Bar-end weights that cut handlebar vibration on longer rides.",
                        950.0, 12),

                // ---------- Mirrors ----------
                product("KTM OEM-Style Rear View Mirror Set", mirrors,
                        "Direct-fit replacement mirror set matching the factory look.",
                        1200.0, 14),
                product("Bar-End Mirror Set (Sport Style)", mirrors,
                        "Bar-end mounted mirror set for a cleaner, sportier cockpit.",
                        1650.0, 8),
                product("Foldable Wing Mirror Set", mirrors,
                        "Impact-foldable wing mirrors that snap back into place after a knock.",
                        1450.0, 10),
                product("Wide-Angle Convex Mirror Set", mirrors,
                        "Wide-angle convex mirror set for better rear visibility in traffic.",
                        1350.0, 9),

                // ---------- Foot Pegs & Rearsets ----------
                product("KTM OEM Rider Foot Peg Set", footPegs,
                        "Direct-fit replacement rider foot peg set.",
                        1100.0, 12),
                product("Adjustable Rearset Kit (Race Spec)", footPegs,
                        "Adjustable rearset kit for a track-focused riding position.",
                        6500.0, 3),
                product("Folding Passenger Foot Peg Set", footPegs,
                        "Spring-loaded folding foot pegs for the pillion.",
                        950.0, 10),
                product("Anti-Slip Foot Peg Rubber Set", footPegs,
                        "Grippy rubber inserts that slot onto the factory foot pegs.",
                        550.0, 18),
                product("Billet Aluminium Foot Peg Set", footPegs,
                        "CNC-machined billet aluminium foot pegs for extra grip and looks.",
                        2200.0, 6),

                // ---------- Suspension & Fork Parts ----------
                product("KTM Fork Oil Seal Kit", suspension,
                        "Replacement fork oil seal kit to stop fork leaks and restore damping.",
                        850.0, 10),
                product("Front Fork Oil (Premium, 1L)", suspension,
                        "Premium fork oil for consistent damping across temperatures.",
                        950.0, 15),
                product("KTM 390 Series Fork Spring Kit", suspension,
                        "Progressive fork spring kit for the 390 Duke and RC 390.",
                        3200.0, 5),
                product("Adjustable Preload Rear Shock (390 Duke/RC)", suspension,
                        "Preload-adjustable rear shock upgrade for the 390 Duke and RC 390.",
                        12500.0, 2),
                product("WP Suspension Rear Shock (790/890 Duke)", suspension,
                        "WP performance rear shock sized for the 790 Duke and 890 Duke R.",
                        18500.0, 1),
                product("Steering Damper Kit (Universal)", suspension,
                        "Bolt-on steering damper kit for added high-speed stability.",
                        8900.0, 3),

                // ---------- Radiator & Cooling ----------
                product("KTM Radiator Guard (Duke/RC Series)", cooling,
                        "Mesh radiator guard that protects against stone chips and debris.",
                        1450.0, 12),
                product("Radiator Coolant (Genuine, 1L)", cooling,
                        "Genuine-spec coolant for reliable year-round cooling performance.",
                        450.0, 30),
                product("Radiator Hose Kit", cooling,
                        "Replacement radiator hose kit for a leak-free cooling system.",
                        1650.0, 8),
                product("Oil Cooler Guard (790/890/1290)", cooling,
                        "Protective guard for the oil cooler on the 790 Duke, 890 Duke R and 1290 Super Duke R.",
                        2400.0, 5),
                product("Cooling Fan Assembly", cooling,
                        "Replacement cooling fan assembly for consistent engine temperatures.",
                        3200.0, 4),

                // ---------- Cables & Wiring ----------
                product("Clutch Cable (Duke/RC Series)", cablesWiring,
                        "Replacement clutch cable for the KTM Duke and RC range.",
                        450.0, 20),
                product("Throttle Cable Set", cablesWiring,
                        "Replacement throttle cable set (open and close) for smooth response.",
                        550.0, 18),
                product("Speedometer Cable", cablesWiring,
                        "Replacement mechanical speedometer cable.",
                        380.0, 15),
                product("Wiring Harness Repair Kit", cablesWiring,
                        "Connector and wiring repair kit for common electrical faults.",
                        1200.0, 10),
                product("Handlebar Switch Assembly", cablesWiring,
                        "Replacement left/right handlebar switch assembly.",
                        1650.0, 8),

                // ---------- Lighting & Bulbs ----------
                product("KTM LED Headlight Assembly", lighting,
                        "Direct-fit LED headlight assembly with a sharper beam pattern.",
                        4200.0, 6),
                product("LED Indicator Set (Front + Rear)", lighting,
                        "Bright, low-draw LED indicator set, front and rear.",
                        1650.0, 12),
                product("LED Tail Light Assembly", lighting,
                        "Direct-fit LED tail light assembly.",
                        1450.0, 10),
                product("Halogen Headlight Bulb (Replacement)", lighting,
                        "Standard replacement halogen headlight bulb.",
                        450.0, 25),
                product("Number Plate LED Light", lighting,
                        "Compact LED number plate light, direct-fit.",
                        350.0, 20),
                product("DRL Auxiliary Light Kit", lighting,
                        "Auxiliary daytime running light kit for extra visibility.",
                        2200.0, 8),

                // ---------- Seat & Comfort ----------
                product("KTM Comfort Gel Seat", seating,
                        "Gel-padded seat upgrade for longer, more comfortable rides.",
                        3200.0, 6),
                product("Waterproof Seat Cover", seating,
                        "Weatherproof seat cover that keeps the saddle dry and protected.",
                        850.0, 15),
                product("Rider Backrest Kit", seating,
                        "Adjustable backrest kit for reduced rider fatigue.",
                        2600.0, 5),
                product("Anti-Slip Seat Cover (Racing)", seating,
                        "Grippy racing-style seat cover that keeps the rider locked in place.",
                        1200.0, 10),
                product("Pillion Seat Cushion", seating,
                        "Extra-padded cushion for pillion comfort on longer trips.",
                        1650.0, 8),

                // ---------- Crash Guards & Frame Sliders ----------
                product("KTM 125-250 Duke Crash Guard", crashGuards,
                        "Engine crash guard for the 125, 200 and 250 Duke.",
                        1850.0, 10),
                product("KTM 390 Duke Crash Guard", crashGuards,
                        "Engine crash guard for the 390 Duke.",
                        2100.0, 9),
                product("KTM RC Series Frame Slider Set", crashGuards,
                        "Bolt-on frame slider set for the RC 200 and RC 390.",
                        2400.0, 8),
                product("KTM 390 Adventure Crash Guard (Engine + Tank)", crashGuards,
                        "Combined engine and tank guard set for the 390 Adventure series.",
                        3600.0, 5),
                product("KTM 790/890 Duke Crash Guard", crashGuards,
                        "Engine crash guard for the 790 Duke and 890 Duke R.",
                        4200.0, 4),
                product("KTM 1290 Super Duke R Frame Slider Set", crashGuards,
                        "Frame slider set for the 1290 Super Duke R.",
                        5200.0, 3),
                product("Handlebar Bar-End Crash Protector Set", crashGuards,
                        "Bar-end protectors that take the hit in a low-speed tip-over.",
                        1450.0, 12),

                // ---------- Number Plate & Tail Tidy ----------
                product("KTM Fender Eliminator / Tail Tidy Kit", tailTidy,
                        "Bolt-on fender eliminator kit for a cleaner tail section.",
                        2200.0, 8),
                product("LED Number Plate Light Holder", tailTidy,
                        "Number plate bracket with an integrated LED light.",
                        950.0, 12),
                product("Adjustable Number Plate Bracket", tailTidy,
                        "Angle-adjustable number plate bracket, direct-fit.",
                        750.0, 14),
                product("Mud Flap / Splash Guard", tailTidy,
                        "Rear wheel splash guard that keeps road spray off the tail section.",
                        650.0, 15)
        ));
    }

    private Category getOrCreateCategory(String name, String description) {
        return categoryRepository.findAll().stream()
                .filter(c -> name.equalsIgnoreCase(c.getName()))
                .findFirst()
                .orElseGet(() -> saveCategory(name, description));
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
                             Double price, Integer stock) {
        return Product.builder()
                .name(name)
                .category(category)
                .price(price)
                .description(description)
                .imageUrl(PLACEHOLDER_BASE + category.getName().replace(" ", "+").replace("&", "and"))
                .stock(stock)
                .active(true)
                .build();
    }
}
