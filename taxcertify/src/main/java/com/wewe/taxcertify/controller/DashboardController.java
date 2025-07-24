package com.wewe.taxcertify.controller;

import com.wewe.taxcertify.dto.DashboardRecordDTO;
import com.wewe.taxcertify.service.WithholdingTaxService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.DecimalFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final WithholdingTaxService taxService;

    public DashboardController(WithholdingTaxService taxService) {
        this.taxService = taxService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

        List<DashboardRecordDTO> recentRecords = taxService.findRecent().stream()
                .map(record -> new DashboardRecordDTO(
                        record.getId(),
                        record.getPayer().getName(),
                        record.getPayee().getName(),
                        record.getDate().format(dateFormatter),
                        decimalFormat.format(record.getAmount()),
                        decimalFormat.format(record.getTaxAmount()),
                        record.getDescription()
                ))
                .collect(Collectors.toList());

        model.addAttribute("recentRecords", recentRecords);
        model.addAttribute("totalRecords", taxService.count());
        model.addAttribute("totalTax", decimalFormat.format(taxService.sumTax()));
        model.addAttribute("currentMonth", YearMonth.now().format(DateTimeFormatter.ofPattern("MMMM yyyy")));

        return "dashboard";
    }
}

