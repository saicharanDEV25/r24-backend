package com.r24.security;

import com.r24.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

/**
 * Resolves the logged-in customer's phone number from the request's
 * Bearer token. The shared JwtAuthenticationFilter only validates tokens
 * without populating the SecurityContext, so customer-scoped endpoints
 * read the token directly here instead of relying on Spring Security's
 * Authentication object.
 */
@Component
public class CustomerAuthHelper {

    private final JwtUtil jwtUtil;

    public CustomerAuthHelper(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String resolvePhoneNumber(HttpServletRequest request) {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = header.substring(7);

        if (!jwtUtil.validateToken(token)) {
            throw new RuntimeException("Invalid or expired session, please log in again");
        }

        return jwtUtil.extractUsername(token);
    }
}
