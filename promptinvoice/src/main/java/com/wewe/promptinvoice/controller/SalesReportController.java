package com.wewe.promptinvoice.controller;

import com.wewe.promptinvoice.model.Invoice;
import com.wewe.promptinvoice.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class SalesReportController {

    private final InvoiceService invoiceService;

    @GetMapping("/reports/sales")
    public String salesReport(
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            Model model) {

        if (startDate == null) {
            startDate = LocalDate.now().withDayOfMonth(1); // เริ่มต้นเดือนนี้
        }

        if (endDate == null) {
            endDate = LocalDate.now(); // วันนี้
        }

        // ดึงข้อมูลใบแจ้งหนี้ช่วงวันที่เลือก
        List<Invoice> invoices = invoiceService.findInvoicesByDateRange(startDate, endDate);

        // คำนวณยอดรวมทั้งหมด
        BigDecimal total = invoices.stream()
                .map(i -> BigDecimal.valueOf(i.getTotalAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

        // สร้างข้อมูลกราฟ: รวมยอดขายแยกตามวันที่
        Map<String, BigDecimal> salesByDate = new TreeMap<>(); // TreeMap เพื่อเรียงวันที่

        for (Invoice invoice : invoices) {
            String dateKey = invoice.getCreatedAt().toLocalDate().toString(); // yyyy-MM-dd
            BigDecimal currentSum = salesByDate.getOrDefault(dateKey, BigDecimal.ZERO);
            currentSum = currentSum.add(BigDecimal.valueOf(invoice.getTotalAmount()));
            salesByDate.put(dateKey, currentSum);
        }

        // แปลงข้อมูล salesByDate เป็น List<Map<String,Object>> สำหรับ Thymeleaf + JS
        List<Map<String, Object>> salesChartData = salesByDate.entrySet().stream()
                .map(e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("label", e.getKey());
                    map.put("amount", e.getValue());
                    return map;
                })
                .collect(Collectors.toList());

        model.addAttribute("invoices", invoices);
        model.addAttribute("totalAmountFormatted", decimalFormat.format(total));
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("salesChartData", salesChartData);

        return "sales_report";  // ชื่อไฟล์ thymeleaf ที่ใช้แสดงผล
    }
}
