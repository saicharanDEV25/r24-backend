package com.r24.security;

import com.r24.entity.Customer;
import com.r24.exception.UnauthorizedException;
import com.r24.repository.CustomerRepository;
import com.r24.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

/**
 * Resolves the current customer from the request's Bearer token. The JWT
 * subject is an anonymous device id (see DeviceSessionRequest/CustomerController),
 * not a phone number — there's no login step, so resolveCustomer() creates
 * the Customer row on first use if it doesn't exist yet.
 */
@Component
public class CustomerAuthHelper {

    private final JwtUtil jwtUtil;
    private final CustomerRepository customerRepository;

    public CustomerAuthHelper(JwtUtil jwtUtil, CustomerRepository customerRepository) {
        this.jwtUtil = jwtUtil;
        this.customerRepository = customerRepository;
    }

    public String resolveDeviceId(HttpServletRequest request) {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new UnauthorizedException("Missing device session");
        }

        String token = header.substring(7);

        if (!jwtUtil.validateToken(token)) {
            throw new UnauthorizedException("Invalid or expired device session");
        }

        return jwtUtil.extractUsername(token);
    }

    public Customer resolveCustomer(HttpServletRequest request) {
        String deviceId = resolveDeviceId(request);

        return customerRepository.findByDeviceId(deviceId)
                .orElseGet(() -> customerRepository.save(Customer.forNewDevice(deviceId)));
    }
}
