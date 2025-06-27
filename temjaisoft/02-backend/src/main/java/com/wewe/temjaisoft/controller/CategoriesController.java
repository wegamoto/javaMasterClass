package com.wewe.temjaisoft.controller;

import com.wewe.temjaisoft.model.inventory.Category;
import com.wewe.temjaisoft.service.inventory.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/inventory/categories")
public class CategoriesController {

    private final CategoryService categoryService;

    public CategoriesController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // 🔹 แสดงรายการทั้งหมด
    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "inventory/category-list";
    }

    // 🔹 แสดงฟอร์มเพิ่ม
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new Category());
        return "inventory/category-form";
    }

    // 🔹 แสดงฟอร์มแก้ไข
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Category> category = categoryService.findById(id);
        if (category == null) {
            return "redirect:/inventory/categories";
        }
        model.addAttribute("category", category);
        return "inventory/category-form";
    }

    // 🔹 บันทึกข้อมูล (เพิ่ม/แก้ไข)
    @PostMapping("/save")
    public String saveCategory(@ModelAttribute("category") Category category) {
        categoryService.save(category);
        return "redirect:/inventory/categories";
    }

    // 🔹 ลบ
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
        return "redirect:/inventory/categories";
    }
}

