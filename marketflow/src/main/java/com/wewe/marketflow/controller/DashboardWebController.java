package com.wewe.marketflow.controller;

import com.wewe.marketflow.dto.DashboardSummaryDTO;
import com.wewe.marketflow.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

@Controller
public class DashboardWebController {

    private final DashboardService dashboardService;

    public DashboardWebController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        DashboardSummaryDTO summary = dashboardService.getSummary();
        Map<String, BigDecimal> spendingByCategory = dashboardService.getSpendingByCategory();

        // ตรวจสอบ null
        if (summary.getTaskStatusCount() == null) {
            summary.setTaskStatusCount(new HashMap<>());
        }
        if (spendingByCategory == null) {
            spendingByCategory = new HashMap<>();
        }

        Map<String, Long> taskStatusCount = summary.getTaskStatusCount();

        // แปลง taskStatusCount Map เป็น List ของ key กับ value แยกกัน
        List<String> taskStatusLabels = new ArrayList<>(taskStatusCount.keySet());
        List<Long> taskStatusData = new ArrayList<>(taskStatusCount.values());

        // แปลง spendingByCategory Map เป็น List ของ key กับ value แยกกัน
        List<String> spendingCategoryLabels = new ArrayList<>(spendingByCategory.keySet());
        List<BigDecimal> spendingCategoryData = new ArrayList<>(spendingByCategory.values());

        // แปลง totalBudgetSpent เป็น String พร้อม comma
        String totalBudgetSpentFormatted = "0";
        if (summary.getTotalBudgetSpent() != null) {
            totalBudgetSpentFormatted = NumberFormat.getNumberInstance(Locale.US)
                    .format(summary.getTotalBudgetSpent());
        }

        // ส่งข้อมูลไปยัง view
        model.addAttribute("summary", summary);
        model.addAttribute("totalBudgetSpentFormatted", totalBudgetSpentFormatted);
        model.addAttribute("taskStatusLabels", taskStatusLabels);
        model.addAttribute("taskStatusData", taskStatusData);
        model.addAttribute("spendingCategoryLabels", spendingCategoryLabels);
        model.addAttribute("spendingCategoryData", spendingCategoryData);

        return "dashboard";
    }

}

