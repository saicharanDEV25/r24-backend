package com.r24.controller;

import com.r24.dto.AnalyticsSummaryResponse;
import com.r24.dto.VisitorLogEntry;
import com.r24.service.AnalyticsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    // Public: anonymous visitor pings from every page load on the public site.
    @PostMapping("/visit")
    public void trackVisit(@RequestBody Map<String, String> body, HttpServletRequest request) {
        analyticsService.trackVisit(body.get("visitorId"), body.get("path"), resolveClientIp(request));
    }

    // Render (and most PaaS hosts) sit behind a proxy, so request.getRemoteAddr()
    // returns the proxy's internal address, not the visitor's real IP —
    // the real one is the first entry in X-Forwarded-For when present.
    private String resolveClientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    @PostMapping("/heartbeat")
    public void heartbeat(@RequestBody Map<String, String> body) {
        analyticsService.heartbeat(body.get("visitorId"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/online-count")
    public Map<String, Long> getOnlineCount() {
        return Map.of("onlineCount", analyticsService.getOnlineCount());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/summary")
    public AnalyticsSummaryResponse getSummary() {
        return analyticsService.getSummary();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/visitors")
    public List<VisitorLogEntry> getVisitorLog() {
        return analyticsService.getVisitorLog();
    }
}
