package com.wewe.taxcertify.controller;

import com.wewe.taxcertify.model.TaxCertificate;
import com.wewe.taxcertify.service.TaxCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/taxcertificates")
public class TaxCertificateController {

    private final TaxCertificateService taxCertificateService;

    @Autowired
    public TaxCertificateController(TaxCertificateService taxCertificateService) {
        this.taxCertificateService = taxCertificateService;
    }

    @GetMapping
    public String listCertificates(Model model) {
        List<TaxCertificate> certificates = taxCertificateService.findAll();
        model.addAttribute("certificates", certificates);
        return "taxcertificates/list"; // /templates/taxcertificates/list.html
    }

    @GetMapping("/new")
    public String showNewCertificateForm(Model model) {
        model.addAttribute("cert", new TaxCertificate());
        return "taxcertificates/form"; // /templates/taxcertificates/form.html
    }

    @PostMapping("/save")
    public String saveCertificate(@ModelAttribute("cert") TaxCertificate cert) {
        taxCertificateService.save(cert); // <-- ใช้งานจริง
        return "redirect:/taxcertificates";
    }

    @GetMapping("/reports")
    public String showReports(Model model) {
        model.addAttribute("certificates", taxCertificateService.findAll());
        return "taxcertificates/reports"; // ชื่อไฟล์ Thymeleaf reports.html
    }
}
