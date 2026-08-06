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
}
