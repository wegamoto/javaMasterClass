package com.wewe.marketflow.controller;

import com.wewe.marketflow.model.BudgetItem;
import com.wewe.marketflow.service.BudgetItemService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/budget-items")
public class BudgetItemController {

    private final BudgetItemService budgetItemService;

    public BudgetItemController(BudgetItemService budgetItemService) {
        this.budgetItemService = budgetItemService;
    }

    @GetMapping
    public List<BudgetItem> getAll() {
        return budgetItemService.getAll();
    }

    @PostMapping
    public BudgetItem create(@RequestBody BudgetItem item) {
        // 💡 Fix: ensure amount is not null or invalid
        if (item.getAmount() == null || item.getAmount().signum() < 0) {
            item.setAmount(BigDecimal.ZERO);
        }

        return budgetItemService.save(item);
    }

    @GetMapping("/campaign/{campaignId}")
    public List<BudgetItem> getByCampaign(@PathVariable Long campaignId) {
        return budgetItemService.findByCampaignId(campaignId);
    }
}
