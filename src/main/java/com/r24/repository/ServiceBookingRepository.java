package com.r24.repository;

import com.r24.entity.ServiceBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceBookingRepository extends JpaRepository<ServiceBooking, Long> {

}