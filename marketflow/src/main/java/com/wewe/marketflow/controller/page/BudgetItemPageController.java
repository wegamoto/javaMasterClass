package com.wewe.marketflow.controller.page;

import com.wewe.marketflow.model.BudgetItem;
import com.wewe.marketflow.service.BudgetItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/budget-items")
@RequiredArgsConstructor
public class BudgetItemPageController {

    private final BudgetItemService budgetItemService;

    @GetMapping
    public String listPage(Model model) {
        model.addAttribute("budgetItems", budgetItemService.getAll());
        return "budget-items/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("budgetItem", new BudgetItem());
        return "budget-items/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        BudgetItem budgetItem = budgetItemService.getById(id);
        model.addAttribute("budgetItem", budgetItem);
        return "budget-items/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BudgetItem budgetItem) {
        budgetItemService.save(budgetItem);
        return "redirect:/budget-items";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        budgetItemService.delete(id);
        return "redirect:/budget-items";
    }
}

