package com.wewe.weweShop.controller;

import com.wewe.weweShop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private ProductService productService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "/admin/admin-dashboard";
    }

}
