package com.wewe.temjaiShop.controller;

import com.wewe.temjaiShop.model.Product;
import com.wewe.temjaiShop.model.enums.RoleName;
import com.wewe.temjaiShop.service.ProductService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    private final ProductService productService;

    public DashboardController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Authentication authentication) {

        // เช็คว่าผู้ใช้มี ROLE_ADMIN หรือไม่ โดยใช้ enum Role
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals(RoleName.ROLE_ADMIN.name()));

        model.addAttribute("isAdmin", isAdmin);

        if (isAdmin) {
            List<Product> stockProducts = productService.getTop5LowStockProducts();
            model.addAttribute("stockProducts", stockProducts);
        }

        if (isAdmin) {
            // ดึงรายการสินค้า (เช่น TOP 5 ที่ stock ต่ำสุด หรือทั้งหมด)
            List<Product> stockProducts = productService.getTop5LowStockProducts(); // หรือ getAllProducts()
            model.addAttribute("stockProducts", stockProducts);
        }

        return "dashboard"; // => src/main/resources/templates/dashboard.html
    }
}
