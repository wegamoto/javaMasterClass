package com.servix.maintenance.controller;

import com.servix.maintenance.model.SparePart;
import com.servix.maintenance.repository.SparePartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/spare-parts")
@RequiredArgsConstructor
public class SparePartController {

    private final SparePartRepository sparePartRepository;

    @GetMapping
    public String listSpareParts(Model model) {
        model.addAttribute("spareParts", sparePartRepository.findAll());
        return "spareparts/list"; // ไปยัง list.html
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("sparePart", new SparePart());
        return "spareparts/form"; // ไปยัง form.html
    }

    @PostMapping
    public String create(@ModelAttribute SparePart sparePart) {
        sparePartRepository.save(sparePart);
        return "redirect:/spare-parts";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        SparePart part = sparePartRepository.findById(id).orElseThrow();
        model.addAttribute("sparePart", part);
        return "spareparts/form";
    }

    @PostMapping("/{id}/update-stock")
    public String updateStock(@PathVariable Long id, @RequestParam int quantity) {
        SparePart part = sparePartRepository.findById(id).orElseThrow();
        part.setQuantityInStock(quantity);
        sparePartRepository.save(part);
        return "redirect:/spare-parts";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        sparePartRepository.deleteById(id);
        return "redirect:/spare-parts";
    }
}
