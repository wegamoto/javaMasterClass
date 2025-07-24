package com.wewe.taxcertify.controller;

import com.wewe.taxcertify.model.Payer;
import com.wewe.taxcertify.service.PayerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payers")
public class PayerController {

    private final PayerService payerService;

    public PayerController(PayerService payerService) {
        this.payerService = payerService;
    }

    // แสดงรายการ payer ทั้งหมด
    @GetMapping
    public String list(Model model) {
        model.addAttribute("payers", payerService.findAll());
        return "payer/list";
    }

    // หน้าเพิ่ม payer ใหม่
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("payer", new Payer());
        return "payer/form";
    }

    // บันทึก payer ใหม่
    @PostMapping("/save")
    public String save(@ModelAttribute Payer payer) {
        payerService.save(payer);
        return "redirect:/payers";
    }
}

