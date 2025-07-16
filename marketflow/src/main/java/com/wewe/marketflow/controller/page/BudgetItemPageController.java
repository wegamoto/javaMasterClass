package com.wewe.marketflow.controller.page;

import com.wewe.marketflow.model.BudgetItem;
import com.wewe.marketflow.service.BudgetItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/budget-items")
@RequiredArgsConstructor
public class BudgetItemPageController {

    private final BudgetItemService budgetItemService;

    @GetMapping
    public String listPage(Model model) {
        List<BudgetItem> budgetItems = budgetItemService.getAll();

        // แก้ไขค่า amount ให้มี 2 ตำแหน่งทศนิยม
        List<BudgetItem> formattedItems = budgetItems.stream()
                .map(item -> {
                    if (item.getAmount() != null) {
                        // ปรับ decimal scale เป็น 2 ตำแหน่ง
                        item.setAmount(item.getAmount().setScale(2, RoundingMode.HALF_UP));
                    }
                    return item;
                }).collect(Collectors.toList());

        model.addAttribute("budgetItems", formattedItems);
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
        // ✅ Fix: ป้องกัน amount เป็น null
        if (budgetItem.getAmount() == null) {
            budgetItem.setAmount(BigDecimal.ZERO);
        }

        budgetItemService.save(budgetItem);
        return "redirect:/budget-items";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        budgetItemService.delete(id);
        return "redirect:/budget-items";
    }
}

