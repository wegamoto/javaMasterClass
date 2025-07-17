package com.wewe.promptinvoice.controller;

import com.wewe.promptinvoice.model.Invoice;
import com.wewe.promptinvoice.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ClientReportController {

    @Autowired
    private InvoiceRepository invoiceRepository;

    // ✅ เปลี่ยน URL mapping ให้ตรง path ที่ต้องการ
    @GetMapping("/reports/clients")
    public String showClientReport(Model model) {
        List<Invoice> invoices = invoiceRepository.findAll();

        // ✅ สรุปข้อมูลลูกค้า
        Map<String, List<Invoice>> grouped = invoices.stream()
                .collect(Collectors.groupingBy(Invoice::getClientName));

        List<ClientSummary> clientSummaries = new ArrayList<>();

        for (Map.Entry<String, List<Invoice>> entry : grouped.entrySet()) {
            String clientName = entry.getKey();
            List<Invoice> clientInvoices = entry.getValue();

            long count = clientInvoices.size();
            double total = clientInvoices.stream().mapToDouble(Invoice::getTotalAmount).sum();

            clientSummaries.add(new ClientSummary(clientName, count, total));
        }

        // ✅ ส่งไปยัง Thymeleaf Template
        model.addAttribute("clients", clientSummaries);
        return "clients"; // => ไปหา clients.html ใน /resources/templates/
    }

    public record ClientSummary(String clientName, long invoiceCount, double totalAmount) {}
}

