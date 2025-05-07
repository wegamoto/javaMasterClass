package com.wewe.weweShop.controller;

import com.wewe.weweShop.model.Product;
import com.wewe.weweShop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductService productService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "/admin/admin-dashboard";
    }

    @GetMapping("/product/view")
    @PreAuthorize("hasRole('ADMIN')")
    public String viewStock(Model model) {
        List<Product> stockProducts = productService.getAllProducts(); // ดึงสินค้าทั้งหมด
        model.addAttribute("stockProducts", stockProducts);
        return "/admin/stock-view"; // ชี้ไปที่ไฟล์ HTML ด้านล่าง
    }
}
