package com.wewe.marketflow.service;

import com.wewe.marketflow.dto.DashboardSummaryDTO;

import java.math.BigDecimal;
import java.util.Map;

public interface DashboardService {
    DashboardSummaryDTO getSummary();

    // เพิ่มเมธอดใหม่เพื่อแสดงผลการใช้จ่ายตามหมวดหมู่
    Map<String, BigDecimal> getSpendingByCategory();
}
