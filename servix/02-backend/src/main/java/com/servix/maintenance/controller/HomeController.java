package com.servix.maintenance.controller;

import com.servix.maintenance.service.IWorkOrderService;
import com.servix.maintenance.service.WorkOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.context.annotation.Lazy;

import java.security.Principal;

@Controller
public class HomeController {

    private final IWorkOrderService workOrderService;

    public HomeController(IWorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }

    @GetMapping({"/", "/home"})
    public String home(Model model, Principal principal) {
        // Get logged-in username
        String username = principal != null ? principal.getName() : "User";

        // Add attributes for Thymeleaf
        model.addAttribute("pageTitle", "Home");
        model.addAttribute("user", new Object() {
            public String getUsername() { return username; }
        });

        model.addAttribute("totalWorkOrders", workOrderService.countAll());
        model.addAttribute("inProgress", workOrderService.countInProgress());
        model.addAttribute("completed", workOrderService.countCompleted());
        model.addAttribute("totalTechnicians", workOrderService.countTechnicians());
        model.addAttribute("recentWorkOrders", workOrderService.findRecentWorkOrders());

        return "home"; // => home.html in /resources/templates/
    }
}

