package com.wewe.autogate.controller;

import com.wewe.autogate.dto.DailySummaryDTO;
import com.wewe.autogate.repository.AccessLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private AccessLogRepository accessLogRepository;

    @GetMapping("/dashboard")
    public String showDashboard(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Principal principal,
            Model model) {

        if (startDate == null) startDate = LocalDate.now().minusDays(7); // default = 7 วันย้อนหลัง
        if (endDate == null) endDate = LocalDate.now();

        List<Object[]> rawSummary = accessLogRepository.getDailyAccessSummaryBetween(startDate, endDate);

        List<DailySummaryDTO> summaryList = rawSummary.stream().map(row -> {
            LocalDate date = ((java.sql.Date) row[0]).toLocalDate();
            long allowed = ((Number) row[1]).longValue();
            long denied = ((Number) row[2]).longValue();
            return new DailySummaryDTO(date, allowed, denied);
        }).toList();

        List<String> dates = summaryList.stream().map(d -> d.getDate().toString()).toList();
        List<Long> allowedList = summaryList.stream().map(DailySummaryDTO::getAllowedCount).toList();
        List<Long> deniedList = summaryList.stream().map(DailySummaryDTO::getDeniedCount).toList();

        model.addAttribute("summary", summaryList);
        model.addAttribute("dates", dates);
        model.addAttribute("allowedList", allowedList);
        model.addAttribute("deniedList", deniedList);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("username", principal.getName()); // <<-- ส่งชื่อผู้ใช้ไป

        return "dashboard";
    }


}

