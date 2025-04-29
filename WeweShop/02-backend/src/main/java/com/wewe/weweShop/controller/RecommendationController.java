package com.wewe.weweShop.controller;

import com.wewe.weweShop.model.Product;
import com.wewe.weweShop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class RecommendationController {

    @Autowired
    private ProductService productService;

    // เส้นทาง /recommendations
    @GetMapping("/recommendations")
    public String showRecommendations(Model model) {
        // สมมุติว่าเราจะแสดงสินค้าที่แนะนำจากฐานข้อมูล
        List<Product> recommendedProducts = productService.getAllProducts();
        model.addAttribute("recommendedProducts", recommendedProducts);
        return "recommendations"; // ชื่อหน้า HTML ที่จะเรนเดอร์
    }
}

