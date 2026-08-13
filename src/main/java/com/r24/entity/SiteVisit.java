package com.r24.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "site_visits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiteVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String visitorId;

    private String path;

    // Real client IP via X-Forwarded-For (Render sits behind a proxy); more stable than
    // visitorId, which resets when the visitor clears storage or switches browsers.
    private String ipAddress;

    @Column(nullable = false)
    private LocalDateTime visitedAt;
}
