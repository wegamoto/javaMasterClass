package com.wewe.temjaisoft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/accounting")
public class AccountingController {
    @GetMapping("")
    public String redirectToDashboard() {
        return "accounting/accounting-dashboard";
    }
}
