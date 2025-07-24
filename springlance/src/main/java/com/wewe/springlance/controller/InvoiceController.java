package com.wewe.springlance.controller;

import com.wewe.springlance.model.Invoice;
import com.wewe.springlance.model.User;
import com.wewe.springlance.model.enums.InvoiceStatus;
import com.wewe.springlance.service.InvoiceService;
import com.wewe.springlance.service.ProjectRequestService;
import com.wewe.springlance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private ProjectRequestService projectRequestService;

    @Autowired
    private UserService userService;

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
        invoice.setStatus(InvoiceStatus.PENDING); // ✅ ใช้ enum แทน isPaid
        invoiceService.save(invoice);
        return "redirect:/invoices/project/" + invoice.getProject().getId();
    }

    @GetMapping
    public String listInvoices(Model model, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName()).orElse(null);
        if (currentUser == null) {
            return "redirect:/login";
        }

        List<Invoice> invoices = invoiceService.getInvoicesForUser(currentUser);
        model.addAttribute("invoices", invoices);
        return "invoice/list"; // /templates/invoice/list.html
    }

    @GetMapping("/new")
    public String showNewInvoiceForm(@RequestParam(required = false) Long projectId, Model model, Principal principal) {
        User currentUser = userService.findByEmail(principal.getName()).orElse(null);
        if (currentUser == null) {
            return "redirect:/login";
        }

        // สำหรับแบบฟอร์มสร้าง
        Invoice invoice = new Invoice();
        invoice.setIssueDate(LocalDate.now());
        model.addAttribute("invoice", invoice);

        // โปรเจกต์ที่ user เกี่ยวข้อง (client หรือ freelancer)
        model.addAttribute("projects", projectRequestService.findByUser(currentUser));
        model.addAttribute("selectedProjectId", projectId); // ใช้ pre-select ใน dropdown

        return "invoice/new"; // => /templates/invoice/new.html
    }

}
