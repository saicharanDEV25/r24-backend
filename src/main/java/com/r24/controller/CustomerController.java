package com.r24.controller;

import com.r24.dto.CustomerAuthResponse;
import com.r24.dto.DeviceSessionRequest;
import com.r24.entity.Customer;
import com.r24.exception.BadRequestException;
import com.r24.repository.CustomerRepository;
import com.r24.security.CustomerAuthHelper;
import com.r24.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final CustomerAuthHelper authHelper;
    private final JwtUtil jwtUtil;

    public CustomerController(CustomerRepository customerRepository,
                               CustomerAuthHelper authHelper,
                               JwtUtil jwtUtil) {
        this.customerRepository = customerRepository;
        this.authHelper = authHelper;
        this.jwtUtil = jwtUtil;
    }

    // No login: frontend generates a device id once and calls this on every load to get/create the matching Customer + JWT.
    @PostMapping("/session")
    public CustomerAuthResponse createSession(@RequestBody DeviceSessionRequest request) {

        String deviceId = request.getDeviceId();

        if (deviceId == null || deviceId.isBlank()) {
            throw new BadRequestException("Missing device id");
        }

        Customer customer = findOrCreateCustomer(deviceId);

        String token = jwtUtil.generateToken(deviceId, "DEVICE");

        return new CustomerAuthResponse(token, customer);
    }

    // Two requests for the same brand-new deviceId can race (e.g. React StrictMode's double effect
    // invocation) — if both miss findByDeviceId, only one save() wins on the unique constraint.
    private Customer findOrCreateCustomer(String deviceId) {
        try {
            return customerRepository.findByDeviceId(deviceId)
                    .orElseGet(() -> customerRepository.save(Customer.forNewDevice(deviceId)));
        } catch (DataIntegrityViolationException e) {
            return customerRepository.findByDeviceId(deviceId).orElseThrow(() -> e);
        }
    }

    @GetMapping("/me")
    public Customer getMyProfile(HttpServletRequest request) {
        return authHelper.resolveCustomer(request);
    }

    @PutMapping("/me")
    public Customer updateMyProfile(HttpServletRequest request, @RequestBody Customer updates) {

        Customer customer = authHelper.resolveCustomer(request);

        // Only overwrite fields that were sent; different callers send different subsets.
        if (updates.getName() != null) customer.setName(updates.getName());
        if (updates.getEmail() != null) customer.setEmail(updates.getEmail());
        if (updates.getBikeModel() != null) customer.setBikeModel(updates.getBikeModel());
        if (updates.getPurchaseYear() != null) customer.setPurchaseYear(updates.getPurchaseYear());
        if (updates.getPhoneNumber() != null) customer.setPhoneNumber(updates.getPhoneNumber());

        return customerRepository.save(customer);
    }
}
