package com.wewe.cashflow.controller;

import com.wewe.cashflow.model.Transaction;
import com.wewe.cashflow.service.CashFlowService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cashflow")
public class CashFlowController {
    private final CashFlowService cashFlowService;

    public CashFlowController(CashFlowService cashFlowService) {
        this.cashFlowService = cashFlowService;
    }

    @PostMapping("/income")
    public String addIncome(@RequestParam double amount, @RequestParam String description) {
        cashFlowService.recordIncome(amount, description);
        return "Income recorded successfully";
    }

    @PostMapping("/expense")
    public String addExpense(@RequestParam double amount, @RequestParam String description) {
        cashFlowService.recordExpense(amount, description);
        return "Expense recorded successfully";
    }

    @GetMapping("/balance")
    public double getBalance() {
        return cashFlowService.getBalance();
    }

    @GetMapping("/transactions")
    public List<Transaction> getTransactions() {
        return cashFlowService.getTransactions();
    }
}
