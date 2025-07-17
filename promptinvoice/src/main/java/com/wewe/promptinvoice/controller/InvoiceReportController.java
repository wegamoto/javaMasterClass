package com.wewe.promptinvoice.controller;

import com.wewe.promptinvoice.model.Invoice;
import com.wewe.promptinvoice.model.InvoiceStatus;
import com.wewe.promptinvoice.repository.InvoiceRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reports")
public class InvoiceReportController {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @GetMapping("/invoices")
    public String showInvoiceReport(Model model) {
        List<Invoice> invoices = invoiceRepository.findAll();

        // แปลง Invoice เป็น InvoiceViewModel พร้อมแปลงวันที่เป็น String ฟอร์แมต dd/MM/yyyy
        List<InvoiceViewModel> invoiceViewModels = invoices.stream().map(invoice -> {
            InvoiceViewModel vm = new InvoiceViewModel();
            vm.setId(invoice.getId());
            vm.setClientName(invoice.getClientName());
            vm.setTotalAmount(BigDecimal.valueOf(invoice.getTotalAmount()));
            vm.setStatus(invoice.getStatus());
            if (invoice.getCreatedAt() != null) {
                LocalDate createdDate = LocalDate.from(invoice.getCreatedAt());
                vm.setCreatedAtFormatted(createdDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            } else {
                vm.setCreatedAtFormatted("");
            }
            return vm;
        }).collect(Collectors.toList());

        model.addAttribute("invoices", invoiceViewModels);

        return "invoices";  // ชื่อไฟล์ thymeleaf ที่จะสร้าง
    }

    @Getter
    @Setter
    public static class InvoiceViewModel {
        private Long id;
        private String clientName;
        private BigDecimal totalAmount;
        private InvoiceStatus status;
        private String createdAtFormatted;
    }
}
