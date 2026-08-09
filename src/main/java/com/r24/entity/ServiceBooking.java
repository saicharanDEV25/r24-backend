package com.r24.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name is too long")
    @Column(nullable = false)
    private String customerName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Enter a valid 10 digit mobile number")
    @Column(nullable = false)
    private String phoneNumber;

    @NotBlank(message = "Bike model is required")
    @Column(nullable = false)
    private String bikeModel;

    @NotBlank(message = "Service type is required")
    @Column(nullable = false)
    private String serviceType;

    @Size(max = 1000, message = "Description is too long")
    @Column(length = 1000)
    private String problemDescription;

    @NotBlank(message = "Preferred date is required")
    @Column(nullable = false)
    private String bookingDate;

    private String bookingTime;

    private String status = "Pending";
}