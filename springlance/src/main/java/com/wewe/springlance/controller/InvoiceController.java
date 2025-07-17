package com.wewe.springlance.controller;

import com.wewe.springlance.model.Invoice;
import com.wewe.springlance.service.InvoiceService;
import com.wewe.springlance.service.ProjectRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private ProjectRequestService projectRequestService;

    @GetMapping("/project/{projectId}")
    public String viewInvoice(@PathVariable Long projectId, Model model) {
        Invoice invoice = invoiceService.findByProjectId(projectId);
        model.addAttribute("invoice", invoice);
        return "invoice/view"; // /templates/invoice/view.html
    }

    @GetMapping("/project/{projectId}/new")
    public String showCreateInvoiceForm(@PathVariable Long projectId, Model model) {
        Invoice invoice = new Invoice();
        invoice.setProject(projectRequestService.findById(projectId).orElse(null));
        invoice.setIssueDate(LocalDate.now());
        model.addAttribute("invoice", invoice);
        return "invoice/create"; // /templates/invoice/create.html
    }

    @PostMapping("/save")
    public String saveInvoice(@ModelAttribute Invoice invoice) {
        invoice.setIsPaid(false);
        invoiceService.save(invoice);
        return "redirect:/invoices/project/" + invoice.getProject().getId();
    }
}
