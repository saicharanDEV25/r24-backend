package com.r24.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpVerifyRequest {

    private String phoneNumber;
    private String code;
}
