package com.wewe.autogate.dto;

import java.time.LocalDate;

public class DailySummaryDTO {
    private LocalDate date;
    private long allowedCount;
    private long deniedCount;

    public DailySummaryDTO(LocalDate date, long allowedCount, long deniedCount) {
        this.date = date;
        this.allowedCount = allowedCount;
        this.deniedCount = deniedCount;
    }

    public LocalDate getDate() { return date; }
    public long getAllowedCount() { return allowedCount; }
    public long getDeniedCount() { return deniedCount; }
}

