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

    // Anonymous per-browser identity (a UUID generated client-side and kept
    // in localStorage) — this is what identifies a returning customer now,
    // not phone/OTP login. phoneNumber below is just an optional profile
    // field, kept in sync from the booking form so "My Garage" can look up
    // service history; it's no longer a login credential.
    //
    // Deliberately NOT nullable=false here: with ddl-auto=update, adding a
    // NOT NULL column to a table that already has rows fails outright on
    // Postgres. Every row the app itself creates always sets this (see
    // CustomerController/CustomerAuthHelper), so it's enforced in practice;
    // this just keeps the migration safe against any pre-existing rows.
    @Column(unique = true)
    private String deviceId;

    private String phoneNumber;

    private String name;

    private String email;

    private String bikeModel;

    private Integer purchaseYear;

    private LocalDateTime createdAt;

    // The phone_number column predates deviceId and still carries the old
    // NOT NULL + UNIQUE constraint in the actual database — ddl-auto=update
    // only ever adds, it doesn't relax existing constraints when the entity
    // annotation changes. Seeding phoneNumber with the (already-unique)
    // deviceId satisfies that leftover constraint without a real phone
    // number; BookingModal overwrites it with the real one on first booking.
    public static Customer forNewDevice(String deviceId) {
        return Customer.builder()
                .deviceId(deviceId)
                .phoneNumber(deviceId)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
