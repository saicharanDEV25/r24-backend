package com.r24.controller;

import com.r24.dto.AnalyticsSummaryResponse;
import com.r24.service.AnalyticsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @PostMapping("/visit")
    public void trackVisit(@RequestBody Map<String, String> body) {
        analyticsService.trackVisit(body.get("visitorId"), body.get("path"));
    }

    @PostMapping("/heartbeat")
    public void heartbeat(@RequestBody Map<String, String> body) {
        analyticsService.heartbeat(body.get("visitorId"));
    }

    @GetMapping("/online-count")
    public Map<String, Long> getOnlineCount() {
        return Map.of("onlineCount", analyticsService.getOnlineCount());
    }

    @GetMapping("/summary")
    public AnalyticsSummaryResponse getSummary() {
        return analyticsService.getSummary();
    }
}
