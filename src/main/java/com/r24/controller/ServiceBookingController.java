package com.r24.controller;

import com.r24.entity.Customer;
import com.r24.entity.ServiceBooking;
import com.r24.repository.ServiceBookingRepository;
import com.r24.security.CustomerAuthHelper;
import com.r24.service.ServiceBookingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class ServiceBookingController {

    private final ServiceBookingService service;
    private final ServiceBookingRepository repository;
    private final CustomerAuthHelper authHelper;

    public ServiceBookingController(ServiceBookingService service,
                                     ServiceBookingRepository repository,
                                     CustomerAuthHelper authHelper) {
        this.service = service;
        this.repository = repository;
        this.authHelper = authHelper;
    }

    @GetMapping("/my")
    public List<ServiceBooking> getMyBookings(HttpServletRequest request) {
        // Matched by phone number from the customer's profile, not a login; no phone on file means no history.
        Customer customer = authHelper.resolveCustomer(request);
        if (customer.getPhoneNumber() == null) {
            return Collections.emptyList();
        }
        return repository.findByPhoneNumber(customer.getPhoneNumber());
    }

    // Public: a customer creates their own booking here, no login involved.
    @PostMapping
    public ServiceBooking addBooking(@Valid @RequestBody ServiceBooking booking) {
        return service.addBooking(booking);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<ServiceBooking> getAllBookings() {
        return service.getAllBookings();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ServiceBooking getBookingById(@PathVariable Long id) {
        return service.getBookingById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ServiceBooking updateBooking(@PathVariable Long id,
                                        @RequestBody ServiceBooking booking) {
        return service.updateBooking(id, booking);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteBooking(@PathVariable Long id) {
        service.deleteBooking(id);
        return "Booking Deleted Successfully";
    }
}