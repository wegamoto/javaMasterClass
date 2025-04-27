package com.wewe.weweShop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import com.wewe.weweShop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/admin/orders")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String showOrders(Model model) {
        model.addAttribute("orders", orderService.findAll());
        model.addAttribute("hasNewOrders", orderService.hasNewOrders());
        return "admin/orders"; // สร้างหน้า orders.html เพิ่มได้
    }
}

