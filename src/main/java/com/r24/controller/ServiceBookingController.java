package com.r24.controller;

import com.r24.entity.ServiceBooking;
import com.r24.repository.ServiceBookingRepository;
import com.r24.security.CustomerAuthHelper;
import com.r24.service.ServiceBookingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

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
        String phone = authHelper.resolvePhoneNumber(request);
        return repository.findByPhoneNumber(phone);
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