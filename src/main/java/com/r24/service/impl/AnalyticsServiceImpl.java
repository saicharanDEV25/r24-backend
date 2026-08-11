package com.r24.service.impl;

import com.r24.dto.AnalyticsSummaryResponse;
import com.r24.dto.DailyVisitStat;
import com.r24.dto.VisitorLogEntry;
import com.r24.entity.SiteVisit;
import com.r24.repository.SiteVisitRepository;
import com.r24.service.AnalyticsService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final Duration ONLINE_WINDOW = Duration.ofSeconds(90);

    private final SiteVisitRepository repository;
    private final Map<String, Instant> lastSeen = new ConcurrentHashMap<>();

    public AnalyticsServiceImpl(SiteVisitRepository repository) {
        this.repository = repository;
    }

    @Override
    public SiteVisit trackVisit(String visitorId, String path, String ipAddress) {
        heartbeat(visitorId);

        return repository.save(
                SiteVisit.builder()
                        .visitorId(visitorId)
                        .path(path)
                        .ipAddress(ipAddress)
                        .visitedAt(LocalDateTime.now())
                        .build()
        );
    }

    @Override
    public List<VisitorLogEntry> getVisitorLog(String range) {
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();

        if ("today".equalsIgnoreCase(range)) {
            return repository.findVisitorLogBetween(startOfToday, startOfToday.plusDays(1));
        }

        if ("yesterday".equalsIgnoreCase(range)) {
            return repository.findVisitorLogBetween(startOfToday.minusDays(1), startOfToday);
        }

        return repository.findVisitorLog();
    }

    @Override
    public void deleteVisitorLog(String ipAddress) {
        repository.deleteByIpAddress(ipAddress);
    }

    @Override
    public void heartbeat(String visitorId) {
        lastSeen.put(visitorId, Instant.now());
    }

    @Override
    public long getOnlineCount() {
        Instant cutoff = Instant.now().minus(ONLINE_WINDOW);
        return lastSeen.values().stream()
                .filter(seenAt -> seenAt.isAfter(cutoff))
                .count();
    }

    @Override
    public AnalyticsSummaryResponse getSummary() {

        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime startOfWeek = startOfToday.minusDays(6);
        LocalDateTime startOfMonth = startOfToday.minusDays(29);

        long visitsToday = repository.countDistinctIpsSince(startOfToday);
        long visitsThisWeek = repository.countDistinctIpsSince(startOfWeek);
        long visitsThisMonth = repository.countDistinctIpsSince(startOfMonth);

        long uniqueToday = repository.countDistinctVisitorsSince(startOfToday);
        long uniqueThisWeek = repository.countDistinctVisitorsSince(startOfWeek);
        long uniqueThisMonth = repository.countDistinctVisitorsSince(startOfMonth);

        long totalVisitors = repository.countDistinctIpsTotal();

        List<DailyVisitStat> last7Days = buildLast7DaysStats(startOfWeek);

        return new AnalyticsSummaryResponse(
                visitsToday,
                visitsThisWeek,
                visitsThisMonth,
                uniqueToday,
                uniqueThisWeek,
                uniqueThisMonth,
                totalVisitors,
                last7Days
        );
    }

    private List<DailyVisitStat> buildLast7DaysStats(LocalDateTime since) {

        List<SiteVisit> recentVisits = repository.findByVisitedAtAfter(since);

        Map<LocalDate, List<SiteVisit>> byDay = recentVisits.stream()
                .collect(Collectors.groupingBy(v -> v.getVisitedAt().toLocalDate()));

        return java.util.stream.IntStream.rangeClosed(0, 6)
                .mapToObj(i -> LocalDate.now().minusDays(6 - i))
                .map(day -> {
                    List<SiteVisit> dayVisits = byDay.getOrDefault(day, List.of());
                    long visits = dayVisits.size();
                    long uniqueVisitors = dayVisits.stream()
                            .map(SiteVisit::getVisitorId)
                            .distinct()
                            .count();
                    return new DailyVisitStat(day.format(DATE_FORMAT), visits, uniqueVisitors);
                })
                .collect(Collectors.toList());
    }
}
