package com.wewe.weweShop.controller;

import com.wewe.weweShop.model.Product;
import com.wewe.weweShop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class DashboardController {

    private final ProductService productService;

    @Autowired
    public DashboardController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Authentication authentication, Principal principal) {
        String userEmail = principal.getName();

        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("username", authentication.getName());

        if (isAdmin) {
            // ดึงรายการสินค้า (เช่น TOP 5 ที่ stock ต่ำสุด หรือทั้งหมด)
            List<Product> stockProducts = productService.getTop5LowStockProducts(); // หรือ getAllProducts()
            model.addAttribute("stockProducts", stockProducts);
        }

        return "dashboard"; // => src/main/resources/templates/dashboard.html
    }
}
