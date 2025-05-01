package com.wewe.weweShop.controller;

import com.wewe.weweShop.service.ProductService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class HomeController {


    private ProductService productService;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("title", "Welcome to Home Page");
        model.addAttribute("products", productService.getFeaturedProducts());
        return "home"; // จะโหลด templates/home.html
    }
}
