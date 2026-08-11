package com.r24.service;

import com.r24.dto.AnalyticsSummaryResponse;
import com.r24.dto.VisitorLogEntry;
import com.r24.entity.SiteVisit;

import java.util.List;

public interface AnalyticsService {

    SiteVisit trackVisit(String visitorId, String path, String ipAddress);

    AnalyticsSummaryResponse getSummary();

    void heartbeat(String visitorId);

    long getOnlineCount();

    List<VisitorLogEntry> getVisitorLog();

    void deleteVisitorLog(String ipAddress);
}
