package com.r24.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Identifies a returning customer; phoneNumber below is just a profile field, not a login credential.
    // Not nullable=false: ddl-auto=update would fail adding NOT NULL to a table with existing rows.
    @Column(unique = true)
    private String deviceId;

    private String phoneNumber;

    private String name;

    private String email;

    private String bikeModel;

    private Integer purchaseYear;

    private LocalDateTime createdAt;

    // phone_number still has a legacy NOT NULL+UNIQUE DB constraint ddl-auto=update won't relax,
    // so seed it with deviceId until BookingModal sets the real number.
    public static Customer forNewDevice(String deviceId) {
        return Customer.builder()
                .deviceId(deviceId)
                .phoneNumber(deviceId)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
