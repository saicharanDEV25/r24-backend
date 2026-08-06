package com.r24.repository;

import com.r24.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {

    Optional<OtpVerification> findTopByPhoneNumberAndUsedFalseOrderByCreatedAtDesc(String phoneNumber);
}
