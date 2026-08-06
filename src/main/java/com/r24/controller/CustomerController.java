package com.r24.controller;

import com.r24.entity.Customer;
import com.r24.exception.ResourceNotFoundException;
import com.r24.repository.CustomerRepository;
import com.r24.security.CustomerAuthHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final CustomerAuthHelper authHelper;

    public CustomerController(CustomerRepository customerRepository, CustomerAuthHelper authHelper) {
        this.customerRepository = customerRepository;
        this.authHelper = authHelper;
    }

    @GetMapping("/me")
    public Customer getMyProfile(HttpServletRequest request) {

        String phone = authHelper.resolvePhoneNumber(request);

        return customerRepository.findByPhoneNumber(phone)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }

    @PutMapping("/me")
    public Customer updateMyProfile(HttpServletRequest request, @RequestBody Customer updates) {

        String phone = authHelper.resolvePhoneNumber(request);

        Customer customer = customerRepository.findByPhoneNumber(phone)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        customer.setName(updates.getName());
        customer.setEmail(updates.getEmail());
        customer.setBikeModel(updates.getBikeModel());
        customer.setPurchaseYear(updates.getPurchaseYear());

        return customerRepository.save(customer);
    }
}
