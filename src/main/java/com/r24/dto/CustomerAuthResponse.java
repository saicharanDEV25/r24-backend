package com.r24.dto;

import com.r24.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerAuthResponse {

    private String token;
    private Customer customer;
}
