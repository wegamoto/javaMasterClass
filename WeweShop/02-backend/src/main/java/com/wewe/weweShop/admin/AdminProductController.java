package com.wewe.weweShop.controller;

import org.springframework.ui.Model;
import com.wewe.weweShop.service.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/products")
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductController {
    private final ProductService productService;
    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String listAdminProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        return "admin/products";
    }
}

