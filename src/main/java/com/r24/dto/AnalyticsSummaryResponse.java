package com.r24.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AnalyticsSummaryResponse {

    private long visitsToday;
    private long visitsThisWeek;
    private long visitsThisMonth;

    private long uniqueVisitorsToday;
    private long uniqueVisitorsThisWeek;
    private long uniqueVisitorsThisMonth;

    private long totalVisitors;

    private List<DailyVisitStat> last7Days;
}
