package com.r24.controller;

import com.r24.dto.CustomerAuthResponse;
import com.r24.dto.OtpRequest;
import com.r24.dto.OtpVerifyRequest;
import com.r24.entity.Customer;
import com.r24.entity.OtpVerification;
import com.r24.exception.BadRequestException;
import com.r24.repository.CustomerRepository;
import com.r24.repository.OtpVerificationRepository;
import com.r24.security.jwt.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/auth/customer")
@CrossOrigin(origins = "*")
public class CustomerAuthController {

    private final OtpVerificationRepository otpRepository;
    private final CustomerRepository customerRepository;
    private final JwtUtil jwtUtil;

    public CustomerAuthController(OtpVerificationRepository otpRepository,
                                   CustomerRepository customerRepository,
                                   JwtUtil jwtUtil) {
        this.otpRepository = otpRepository;
        this.customerRepository = customerRepository;
        this.jwtUtil = jwtUtil;
    }

    /**
     * NOTE: No SMS gateway is configured yet, so the OTP is returned in the
     * response as "devOtp" instead of being texted to the phone. This makes
     * the login flow fully testable end-to-end today. Once an SMS provider
     * (e.g. Fast2SMS, MSG91, Twilio) is set up, send `code` via that
     * provider here and drop `devOtp` from the response.
     */
    @PostMapping("/request-otp")
    public Map<String, Object> requestOtp(@RequestBody OtpRequest request) {

        String phone = request.getPhoneNumber();

        if (phone == null || !phone.matches("^[6-9]\\d{9}$")) {
            throw new BadRequestException("Enter a valid 10 digit mobile number");
        }

        String code = String.valueOf(100000 + new Random().nextInt(900000));

        OtpVerification otp = OtpVerification.builder()
                .phoneNumber(phone)
                .code(code)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .used(false)
                .build();

        otpRepository.save(otp);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "OTP generated");
        response.put("devOtp", code);
        return response;
    }

    @PostMapping("/verify-otp")
    public CustomerAuthResponse verifyOtp(@RequestBody OtpVerifyRequest request) {

        OtpVerification otp = otpRepository
                .findTopByPhoneNumberAndUsedFalseOrderByCreatedAtDesc(request.getPhoneNumber())
                .orElseThrow(() -> new BadRequestException("No OTP found, please request a new one"));

        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("OTP expired, please request a new one");
        }

        if (!otp.getCode().equals(request.getCode())) {
            throw new BadRequestException("Incorrect OTP");
        }

        otp.setUsed(true);
        otpRepository.save(otp);

        Customer customer = customerRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseGet(() -> customerRepository.save(
                        Customer.builder()
                                .phoneNumber(request.getPhoneNumber())
                                .createdAt(LocalDateTime.now())
                                .build()
                ));

        String token = jwtUtil.generateToken(customer.getPhoneNumber());

        return new CustomerAuthResponse(token, customer);
    }
}
