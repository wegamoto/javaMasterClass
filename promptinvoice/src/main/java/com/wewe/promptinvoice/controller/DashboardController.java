package com.wewe.promptinvoice.controller;

import com.wewe.promptinvoice.model.Invoice;
import com.wewe.promptinvoice.model.InvoiceStatus;
import com.wewe.promptinvoice.service.InvoiceService;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final InvoiceService invoiceService;

    public DashboardController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        long totalInvoices = invoiceService.countAllInvoices();
        double totalAmount = invoiceService.sumTotalAmount();
        long totalClients = invoiceService.countDistinctClients();
        long overdueInvoices = invoiceService.countOverdueInvoices();

        long paidInvoices = invoiceService.countInvoicesByStatus(InvoiceStatus.PAID);
        long unpaidInvoices = invoiceService.countInvoicesByStatus(InvoiceStatus.UNPAID);


//        // ✅ เพิ่ม: การนับจำนวนใบแจ้งหนี้ที่ยังค้างชำระ (UNPAID)
//        long unpaidInvoices = invoiceService.findAll().stream()
//                .filter(i -> i.getStatus() == InvoiceStatus.UNPAID)
//                .count();

        // ฟอร์แมตตัวเลขให้พร้อมใช้ใน Thymeleaf
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        String totalAmountFormatted = decimalFormat.format(totalAmount);

        List<Invoice> recentInvoices = invoiceService.findRecentInvoices(5);
        List<InvoiceViewModel> recentInvoiceViews = recentInvoices.stream()
                .map(InvoiceViewModel::fromInvoice)
                .collect(Collectors.toList());

        model.addAttribute("totalInvoices", totalInvoices);
        model.addAttribute("totalAmount", totalAmount); // ใช้ได้ถ้าจะเปรียบเทียบตัวเลข
        model.addAttribute("totalAmountFormatted", totalAmountFormatted); // สำหรับแสดง
        model.addAttribute("totalClients", totalClients);
        model.addAttribute("overdueInvoices", overdueInvoices);
        model.addAttribute("recentInvoices", recentInvoiceViews);
        model.addAttribute("paidInvoices", paidInvoices);
        model.addAttribute("unpaidInvoices", unpaidInvoices);

        return "dashboard";
    }

    // ViewModel สำหรับใช้แสดงผลใน Template
    @Getter
    public static class InvoiceViewModel {
        private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        private static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat("#,##0.00");

        private final Long id;
        private final String clientName;
        private final String createdAt;
        private final Double totalAmount;
        private final String totalAmountFormatted;
        private final InvoiceStatus status;
        private final String statusDisplayName;

        public InvoiceViewModel(Long id, String clientName, String createdAt,
                                Double totalAmount, String totalAmountFormatted,
                                InvoiceStatus status, String statusDisplayName) {
            this.id = id;
            this.clientName = clientName;
            this.createdAt = createdAt;
            this.totalAmount = totalAmount;
            this.totalAmountFormatted = totalAmountFormatted;
            this.status = status;
            this.statusDisplayName = statusDisplayName != null ? statusDisplayName : "";
        }

        public static InvoiceViewModel fromInvoice(Invoice invoice) {
            String formattedDate = invoice.getCreatedAt() != null
                    ? invoice.getCreatedAt().format(DATE_FORMATTER)
                    : "";
            String formattedAmount = DECIMAL_FORMATTER.format(invoice.getTotalAmount());
            String displayName = invoice.getStatus() != null ? invoice.getStatus().getDisplayName() : "";

            return new InvoiceViewModel(
                    invoice.getId(),
                    invoice.getClientName(),
                    formattedDate,
                    invoice.getTotalAmount(),
                    formattedAmount,
                    invoice.getStatus(),
                    displayName
            );
        }
    }
}
