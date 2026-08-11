package com.r24.repository;

import com.r24.entity.SiteVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface SiteVisitRepository extends JpaRepository<SiteVisit, Long> {

    long countByVisitedAtAfter(LocalDateTime since);

    @Query("SELECT COUNT(DISTINCT s.visitorId) FROM SiteVisit s WHERE s.visitedAt >= :since")
    long countDistinctVisitorsSince(LocalDateTime since);

    // "Visits" is meant to mean distinct people, not raw pings — someone opening
    // the site 5 times today should still only count as 1 visit today, not 5.
    @Query("SELECT COUNT(DISTINCT s.ipAddress) FROM SiteVisit s WHERE s.visitedAt >= :since")
    long countDistinctIpsSince(LocalDateTime since);

    @Query("SELECT COUNT(DISTINCT s.ipAddress) FROM SiteVisit s")
    long countDistinctIpsTotal();

    @Modifying
    @Transactional
    @Query("DELETE FROM SiteVisit s WHERE s.ipAddress = :ipAddress")
    void deleteByIpAddress(@Param("ipAddress") String ipAddress);

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

    // Same shape as findVisitorLog(), but scoped to visits within [start, end) —
    // powers the Today/Yesterday filter on the visitor log table.
    @Query("SELECT new com.r24.dto.VisitorLogEntry(s.ipAddress, COUNT(s), MIN(s.visitedAt), MAX(s.visitedAt)) " +
           "FROM SiteVisit s " +
           "WHERE s.visitedAt >= :start AND s.visitedAt < :end " +
           "GROUP BY s.ipAddress " +
           "ORDER BY COUNT(s) DESC")
    List<com.r24.dto.VisitorLogEntry> findVisitorLogBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
