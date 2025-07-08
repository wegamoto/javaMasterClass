package com.wewe.proflow.controller.web;

import com.wewe.proflow.model.CashFlow;
import com.wewe.proflow.service.CashFlowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cashflows")
@RequiredArgsConstructor
public class CashFlowController {

    private final CashFlowService cashFlowService;

    @GetMapping
    public String listCashFlows(Model model) {
        model.addAttribute("cashflows", cashFlowService.findAll());
        return "cashflow"; // cashflow.html
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("cashFlow", new CashFlow());
        return "cashflow-form"; // cashflow-form.html
    }

    @PostMapping("/save")
    public String saveCashFlow(@ModelAttribute("cashFlow") CashFlow cashFlow) {
        cashFlowService.save(cashFlow);
        return "redirect:/cashflows";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        CashFlow cashFlow = cashFlowService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid CashFlow ID: " + id));
        model.addAttribute("cashFlow", cashFlow);
        return "cashflow-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteCashFlow(@PathVariable Long id) {
        cashFlowService.deleteById(id);
        return "redirect:/cashflows";
    }
}
