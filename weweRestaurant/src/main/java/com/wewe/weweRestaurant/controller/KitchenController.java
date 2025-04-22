package com.wewe.weweRestaurant.controller;

import com.wewe.weweRestaurant.model.Order;
import com.wewe.weweRestaurant.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/kitchen")
public class KitchenController {

    private final OrderRepository orderRepo;

    @GetMapping
    public String viewKitchenOrders(Model model) {
        List<Order> orders = orderRepo.findByStatus("PENDING");
        model.addAttribute("orders", orders);
        return "kitchen";
    }

    @PostMapping("/mark-ready/{orderId}")
    public String markOrderReady(@PathVariable Long orderId) {
        Order order = orderRepo.findById(orderId).orElseThrow();
        order.setStatus("READY");
        orderRepo.save(order);
        return "redirect:/kitchen";
    }
}

