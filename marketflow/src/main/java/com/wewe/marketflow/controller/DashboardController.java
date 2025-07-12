package com.wewe.marketflow.controller;

import com.wewe.marketflow.dto.DashboardSummaryDTO;
import com.wewe.marketflow.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public DashboardSummaryDTO getSummary() {
        return dashboardService.getSummary();
    }
}

