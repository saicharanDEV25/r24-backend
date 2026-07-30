package com.r24.service.impl;

import com.r24.entity.ServiceBooking;
import com.r24.repository.ServiceBookingRepository;
import com.r24.service.ServiceBookingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceBookingServiceImpl implements ServiceBookingService {

    private final ServiceBookingRepository repository;

    public ServiceBookingServiceImpl(ServiceBookingRepository repository) {
        this.repository = repository;
    }

    @Override
    public ServiceBooking addBooking(ServiceBooking booking) {
        return repository.save(booking);
    }

    @Override
    public List<ServiceBooking> getAllBookings() {
        return repository.findAll();
    }

    @Override
    public ServiceBooking getBookingById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public ServiceBooking updateBooking(Long id, ServiceBooking booking) {

        ServiceBooking existing = repository.findById(id).orElse(null);

        if (existing == null) {
            return null;
        }

        existing.setCustomerName(booking.getCustomerName());
        existing.setPhoneNumber(booking.getPhoneNumber());
        existing.setBikeModel(booking.getBikeModel());
        existing.setServiceType(booking.getServiceType());
        existing.setProblemDescription(booking.getProblemDescription());
        existing.setBookingDate(booking.getBookingDate());
        existing.setBookingTime(booking.getBookingTime());
        existing.setStatus(booking.getStatus());

        return repository.save(existing);
    }

    @Override
    public void deleteBooking(Long id) {
        repository.deleteById(id);
    }
}