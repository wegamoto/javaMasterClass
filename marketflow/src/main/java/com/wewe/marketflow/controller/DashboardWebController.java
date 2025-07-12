package com.wewe.marketflow.controller;

import com.wewe.marketflow.dto.DashboardSummaryDTO;
import com.wewe.marketflow.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        // ตรวจสอบให้แน่ใจว่าไม่ null ก่อนใช้งาน
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

        // ส่งข้อมูลไปยัง view
        model.addAttribute("summary", summary);
        model.addAttribute("taskStatusLabels", taskStatusLabels);
        model.addAttribute("taskStatusData", taskStatusData);
        model.addAttribute("spendingCategoryLabels", spendingCategoryLabels);
        model.addAttribute("spendingCategoryData", spendingCategoryData);

        return "dashboard";
    }


}

