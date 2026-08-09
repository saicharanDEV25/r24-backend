package com.r24.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple in-memory per-IP rate limiting for the handful of public endpoints
 * that are worth abuse-proofing (login brute-force, form/booking spam,
 * device-session creation, image upload cost). In-memory is fine for a
 * single Render instance; if this ever runs on more than one instance, the
 * bucket map would need to move to a shared store (e.g. Redis) instead.
 */
@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private record Limit(String method, String path, int capacity, Duration window) {
    }

    private final List<Limit> limits = List.of(
            new Limit("POST", "/api/auth/login", 5, Duration.ofMinutes(15)),
            new Limit("POST", "/api/contact", 5, Duration.ofMinutes(15)),
            new Limit("POST", "/api/bookings", 5, Duration.ofMinutes(15)),
            new Limit("POST", "/api/customers/session", 20, Duration.ofMinutes(15)),
            new Limit("POST", "/api/upload", 20, Duration.ofMinutes(15))
    );

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain)
            throws ServletException, IOException {

        Limit matched = limits.stream()
                .filter(l -> l.method().equalsIgnoreCase(request.getMethod())
                        && l.path().equals(request.getRequestURI()))
                .findFirst()
                .orElse(null);

        if (matched != null) {
            String key = matched.path() + "|" + clientIp(request);
            Bucket bucket = buckets.computeIfAbsent(key, k -> newBucket(matched));

            if (!bucket.tryConsume(1)) {
                response.setStatus(429);
                response.setContentType("application/json");
                response.getWriter().write(
                        "{\"status\":429,\"message\":\"Too many requests. Please wait a bit and try again.\"}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private Bucket newBucket(Limit limit) {
        Bandwidth bandwidth = Bandwidth.classic(
                limit.capacity(),
                Refill.intervally(limit.capacity(), limit.window())
        );
        return Bucket.builder().addLimit(bandwidth).build();
    }

    private String clientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
