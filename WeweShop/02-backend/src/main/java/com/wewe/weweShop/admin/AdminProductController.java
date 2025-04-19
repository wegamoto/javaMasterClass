package com.wewe.weweShop.admin;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String showAdminProductList(Model model) {
        model.addAttribute("products", productService.findAll());
        return "admin/products"; // → templates/admin/products.html
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}

