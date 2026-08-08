package com.r24.controller;

import com.r24.entity.Customer;
import com.r24.entity.ServiceBooking;
import com.r24.repository.ServiceBookingRepository;
import com.r24.security.CustomerAuthHelper;
import com.r24.service.ServiceBookingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
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
        // "My" bookings are still matched by phone number text (same as
        // before), but that phone now comes from the customer's profile —
        // kept in sync from whatever they last typed into the booking form —
        // rather than from a login credential. No phone on file yet (never
        // booked from this device) just means no history.
        Customer customer = authHelper.resolveCustomer(request);
        if (customer.getPhoneNumber() == null) {
            return Collections.emptyList();
        }
        return repository.findByPhoneNumber(customer.getPhoneNumber());
    }

    @PostMapping
    public ServiceBooking addBooking(@RequestBody ServiceBooking booking) {
        return service.addBooking(booking);
    }

    @GetMapping
    public List<ServiceBooking> getAllBookings() {
        return service.getAllBookings();
    }

    @GetMapping("/{id}")
    public ServiceBooking getBookingById(@PathVariable Long id) {
        return service.getBookingById(id);
    }

    @PutMapping("/{id}")
    public ServiceBooking updateBooking(@PathVariable Long id,
                                        @RequestBody ServiceBooking booking) {
        return service.updateBooking(id, booking);
    }

    @DeleteMapping("/{id}")
    public String deleteBooking(@PathVariable Long id) {
        service.deleteBooking(id);
        return "Booking Deleted Successfully";
    }
}