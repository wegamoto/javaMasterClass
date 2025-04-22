package com.wewe.weweRestaurant.controller;

import com.wewe.weweRestaurant.repository.MenuItemRepository;
import com.wewe.weweRestaurant.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final MenuItemRepository menuRepo;
    private final OrderService orderService;

    @GetMapping("/order")
    public String showOrderForm(Model model) {
        model.addAttribute("menuItems", menuRepo.findAll());
        return "order";
    }

    @PostMapping("/order")
    public String submitOrder(@RequestParam Map<String, String> params) {
        Map<Long, Integer> menuItemQuantities = new HashMap<>();
        String tableNumber = params.get("tableNumber");

        for (String key : params.keySet()) {
            if (key.startsWith("menu_")) {
                Long id = Long.parseLong(key.replace("menu_", ""));
                int qty = Integer.parseInt(params.get(key));
                if (qty > 0) {
                    menuItemQuantities.put(id, qty);
                }
            }
        }

        orderService.createOrder(menuItemQuantities, tableNumber);
        return "redirect:/order?success";
    }


}

