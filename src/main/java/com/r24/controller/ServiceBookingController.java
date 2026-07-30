package com.r24.controller;

import com.r24.entity.ServiceBooking;
import com.r24.service.ServiceBookingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class ServiceBookingController {

    private final ServiceBookingService service;

    public ServiceBookingController(ServiceBookingService service) {
        this.service = service;
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