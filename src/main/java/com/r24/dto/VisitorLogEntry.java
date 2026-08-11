package com.r24.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class VisitorLogEntry {

    private String ipAddress;
    private long visitCount;
    private LocalDateTime firstVisit;
    private LocalDateTime lastVisit;
}
