package com.wewe.weweShop.controller;

import com.wewe.weweShop.model.Product;
import com.wewe.weweShop.service.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String showProductList(Model model) {
        model.addAttribute("products", productService.findAll());
        return "products"; // → templates/products.html
    }
}

