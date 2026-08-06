package com.r24.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DailyVisitStat {

    private String date;
    private long visits;
    private long uniqueVisitors;
}
