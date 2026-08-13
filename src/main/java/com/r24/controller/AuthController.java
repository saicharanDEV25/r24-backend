package com.r24.controller;

import com.r24.dto.LoginRequest;
import com.r24.dto.LoginResponse;
import com.r24.exception.UnauthorizedException;
import com.r24.security.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password-hash}")
    private String adminPasswordHash;

    public AuthController(JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        // Always run the hash comparison so a bad username can't be timed against a bad password.
        boolean passwordMatches =
                passwordEncoder.matches(request.getPassword(), adminPasswordHash);
        boolean usernameMatches = adminUsername.equals(request.getUsername());

        if (!usernameMatches || !passwordMatches) {
            throw new UnauthorizedException("Invalid Username or Password");
        }

        String token = jwtUtil.generateToken(request.getUsername(), "ADMIN");

        return new LoginResponse(token);
    }
}
