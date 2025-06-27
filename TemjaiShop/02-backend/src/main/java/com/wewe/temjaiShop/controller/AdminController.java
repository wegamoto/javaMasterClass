package com.wewe.temjaiShop.controller;

import com.wewe.temjaiShop.model.Product;
import com.wewe.temjaiShop.service.ProductService;
import com.wewe.temjaiShop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductService productService;

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminDashboard(Model model) {
        return "admin/admin-dashboard";
    }

    @GetMapping("/product/view")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String viewStock(Model model) {
        List<Product> stockProducts = productService.getAllProducts(); // ดึงสินค้าทั้งหมด
        model.addAttribute("stockProducts", stockProducts);
        return "admin/stock-view"; // ชี้ไปที่ไฟล์ HTML ด้านล่าง
    }

    @PostMapping("/fix-null-roles")
    public ResponseEntity<String> fixRoles() {
        userService.fixNullRoles();
        return ResponseEntity.ok("Roles updated successfully.");
    }
}
