package com.r24.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "service_bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String bikeModel;

    @Column(nullable = false)
    private String serviceType;

    @Column(length = 1000)
    private String problemDescription;

    @Column(nullable = false)
    private String bookingDate;

    private String bookingTime;

    private String status = "Pending";
}