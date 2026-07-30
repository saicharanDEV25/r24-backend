package com.r24.controller;

import com.r24.dto.LoginRequest;
import com.r24.dto.LoginResponse;
import com.r24.security.jwt.JwtUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        if ("admin".equals(request.getUsername())
                && "admin123".equals(request.getPassword())) {

            String token = jwtUtil.generateToken(request.getUsername());

            return new LoginResponse(token);
        }

        throw new RuntimeException("Invalid Username or Password");
    }
}