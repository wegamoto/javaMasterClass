package com.wewe.controller;

import com.wewe.model.ElectricityBill;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/api")
public class ElectricityController {

    @GetMapping("/bills")
    public String home(Model model) {
        model.addAttribute("result", null);
        return "index";
    }

    @PostMapping("/calculate")
    public String calculate(@RequestParam double units, Model model) {
        double total = calculateElectricityBill(units);
        model.addAttribute("result", total);
        return "index";
    }

    private double calculateElectricityBill(double units) {
        double cost = 0;

        if (units <= 150) {
            cost = units * 3.50;
        } else if (units <= 400) {
            cost = (150 * 3.50) + ((units - 150) * 4.20);
        } else {
            cost = (150 * 3.50) + (250 * 4.20) + ((units - 400) * 5.50);
        }

        double ft = units * 0.25; // ค่าไฟแปรผัน FT
        double serviceCharge = 38.22; // ค่าบริการรายเดือน
        return cost + ft + serviceCharge;
    }
}

