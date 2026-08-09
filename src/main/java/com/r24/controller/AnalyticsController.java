package com.r24.controller;

import com.r24.dto.AnalyticsSummaryResponse;
import com.r24.service.AnalyticsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public void trackVisit(@RequestBody Map<String, String> body) {
        analyticsService.trackVisit(body.get("visitorId"), body.get("path"));
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
}
