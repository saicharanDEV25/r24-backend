package com.r24.repository;

import com.r24.entity.SiteVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface SiteVisitRepository extends JpaRepository<SiteVisit, Long> {

    long countByVisitedAtAfter(LocalDateTime since);

    @Query("SELECT COUNT(DISTINCT s.visitorId) FROM SiteVisit s WHERE s.visitedAt >= :since")
    long countDistinctVisitorsSince(LocalDateTime since);

    List<SiteVisit> findByVisitedAtAfter(LocalDateTime since);

    // One row per IP that has ever visited, with how many times and the
    // first/last time — ordered so the most frequent (regular) visitors
    // show up first. IPs are never null (captured server-side on every
    // ping), so this doesn't need a null guard.
    @Query("SELECT new com.r24.dto.VisitorLogEntry(s.ipAddress, COUNT(s), MIN(s.visitedAt), MAX(s.visitedAt)) " +
           "FROM SiteVisit s " +
           "GROUP BY s.ipAddress " +
           "ORDER BY COUNT(s) DESC")
    List<com.r24.dto.VisitorLogEntry> findVisitorLog();
}
