package com.r24.service;

import com.r24.entity.ServiceBooking;

import java.util.List;

public interface ServiceBookingService {

    ServiceBooking addBooking(ServiceBooking booking);

    List<ServiceBooking> getAllBookings();

    ServiceBooking getBookingById(Long id);

    ServiceBooking updateBooking(Long id, ServiceBooking booking);

    void deleteBooking(Long id);
}