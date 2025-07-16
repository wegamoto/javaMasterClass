package com.wewe.promptinvoice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wewe.promptinvoice.model.Invoice;
import com.wewe.promptinvoice.model.Item;
import com.wewe.promptinvoice.repository.InvoiceRepository;
import com.wewe.promptinvoice.service.InvoiceService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class InvoiceController {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceService invoiceService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

    public InvoiceController(InvoiceRepository invoiceRepository, InvoiceService invoiceService) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceService = invoiceService;
    }

    @GetMapping("/invoices")
    public String listInvoices(Model model, HttpServletRequest request) {
        List<Invoice> invoices = invoiceRepository.findAll();
        List<DashboardController.InvoiceViewModel> viewModels = invoices.stream()
                .map(DashboardController.InvoiceViewModel::fromInvoice)
                .toList();

        model.addAttribute("invoices", viewModels);
        model.addAttribute("requestURI", request.getRequestURI());
        return "invoice_list";
    }

    @GetMapping("/invoices/new")
    public String showCreateForm(Model model, HttpServletRequest request) {
        model.addAttribute("invoice", new Invoice());
        model.addAttribute("requestURI", request.getRequestURI());
        return "invoice-form";
    }

    @PostMapping("/invoices")
    public String createInvoice(@ModelAttribute Invoice invoice) {
        invoiceRepository.save(invoice);
        return "redirect:/invoices";
    }

    @GetMapping("/invoices/{id}")
    public String viewInvoice(@PathVariable Long id, Model model, HttpServletRequest request) throws IOException {
        Optional<Invoice> invoiceOpt = invoiceService.findById(id);
        if (invoiceOpt.isEmpty()) return "error/404";

        Invoice invoice = invoiceOpt.get();
        List<Item> items = objectMapper.readValue(invoice.getItemsJson(), new TypeReference<>() {});

        List<FormattedItemViewModel> formattedItems = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (Item item : items) {
            BigDecimal price = BigDecimal.valueOf(item.getPrice());
            int quantity = item.getQuantity();
            BigDecimal lineTotal = price.multiply(BigDecimal.valueOf(quantity));
            subtotal = subtotal.add(lineTotal);

            formattedItems.add(new FormattedItemViewModel(
                    item.getDescription(), price, quantity, decimalFormat
            ));
        }

        // ✅ คำนวณ VAT ถ้ามี
        BigDecimal vatAmount = BigDecimal.ZERO;
        BigDecimal totalWithVat = subtotal;

        if (invoice.isVatApplied()) {
            vatAmount = subtotal.multiply(BigDecimal.valueOf(0.07));
            totalWithVat = subtotal.add(vatAmount);
        }

        // ✅ Format
        String subtotalFormatted = decimalFormat.format(subtotal);
        String vatFormatted = decimalFormat.format(vatAmount);
        String totalAmountFormatted = decimalFormat.format(totalWithVat);

        String createdAtFormatted = invoice.getCreatedAt() != null
                ? invoice.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : "";

        model.addAttribute("invoice", invoice);
        model.addAttribute("items", formattedItems);
        model.addAttribute("subtotalFormatted", subtotalFormatted);  // 💰 ยอดก่อน VAT
        model.addAttribute("vatFormatted", vatFormatted);            // 💸 VAT
        model.addAttribute("totalAmountFormatted", totalAmountFormatted); // ✅ รวม VAT
        model.addAttribute("createdAtFormatted", createdAtFormatted);
        model.addAttribute("requestURI", request.getRequestURI());

        return "invoice_view";
    }


    @Getter
    public static class FormattedItemViewModel {

        private final String description;
        private final BigDecimal price;
        private final int quantity;
        private final String priceFormatted;
        private final String totalFormatted;

        public FormattedItemViewModel(String description, BigDecimal price, int quantity, DecimalFormat df) {
            this.description = description;
            this.price = price;
            this.quantity = quantity;
            this.priceFormatted = df.format(price);
            this.totalFormatted = df.format(price.multiply(BigDecimal.valueOf(quantity)));
        }
    }
}
