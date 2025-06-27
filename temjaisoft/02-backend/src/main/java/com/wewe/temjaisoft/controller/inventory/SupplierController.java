package com.wewe.temjaisoft.controller.inventory;

import com.wewe.temjaisoft.model.inventory.Supplier;
import com.wewe.temjaisoft.service.inventory.SupplierService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/inventory/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public String listSuppliers(Model model) {
        model.addAttribute("suppliers", supplierService.getAll());
        return "inventory/supplier-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("supplier", new Supplier());
        return "inventory/supplier-form";
    }

    @PostMapping
    public String saveSupplier(@ModelAttribute Supplier supplier) {
        supplierService.save(supplier);
        return "redirect:/inventory/suppliers";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        supplierService.getById(id).ifPresentOrElse(
                supplier -> model.addAttribute("supplier", supplier),
                () -> model.addAttribute("supplier", new Supplier())
        );
        return "inventory/supplier-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteSupplier(@PathVariable Long id) {
        supplierService.delete(id);
        return "redirect:/inventory/suppliers";
    }
}
