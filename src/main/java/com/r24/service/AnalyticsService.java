package com.r24.service;

import com.r24.dto.AnalyticsSummaryResponse;
import com.r24.entity.SiteVisit;

public interface AnalyticsService {

    SiteVisit trackVisit(String visitorId, String path);

    AnalyticsSummaryResponse getSummary();

    void heartbeat(String visitorId);

    long getOnlineCount();
}
