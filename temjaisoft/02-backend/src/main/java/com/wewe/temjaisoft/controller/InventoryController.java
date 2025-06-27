package com.wewe.temjaisoft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InventoryController {

    @GetMapping("/inventory")
    public String showInventoryDashboard(Model model) {
        // คุณสามารถเพิ่มข้อมูลสถิติ, รายการสินค้า ฯลฯ ได้ที่นี่
        return "inventory/inventory-dashboard";
    }
}

