package com.wewe.weweShop.controller;

import com.wewe.weweShop.model.Order;
import com.wewe.weweShop.repository.OrderRepository;
import com.wewe.weweShop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class OrderController {

    private final OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public String viewOrders(Model model, Authentication authentication) {
        String email = authentication.getName();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        List<Order> orders;
        if (isAdmin) {
            orders = orderService.getAllOrders(); // แสดงทุกคำสั่งซื้อถ้าเป็นแอดมิน
        } else {
            orders = orderService.getOrdersByEmail(email); // เฉพาะของตัวเองถ้าเป็น user
        }

        model.addAttribute("orders", orders);
        model.addAttribute("isAdmin", isAdmin);
        return "orders";  // ไปยัง orders.html
    }



//    // 🧍 สำหรับผู้ใช้ทั่วไป
//    @GetMapping("/user/orders")
//    public String viewUserOrders(Model model) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String email = auth.getName(); // ใช้ email หรือ username
//        List<Order> orders = orderService.getOrdersByEmail(email);
//        model.addAttribute("orders", orders);
//        return "orders"; // กลับไปยัง orders.html
//    }
//
//    // 🔎 รายละเอียดของออร์เดอร์สำหรับผู้ใช้
//    @GetMapping("/user/orders/{id}")
//    public String viewOrderDetail(@PathVariable Long id, Model model) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String email = auth.getName();
//
//        Order order = orderService.getOrderByIdAndEmail(id, email);
//        model.addAttribute("order", order);
//        return "order-detail"; // กลับไปยัง order-detail.html
//    }
//
//    // 👑 สำหรับแอดมินดูรายการทั้งหมด
//    @GetMapping("/admin/orders")
//    public String viewOrders(Model model) {
//        List<Order> orders = orderRepository.findAll();
//        model.addAttribute("orders", orders);
//        return "order-list"; // กลับไปยัง order-list.html
//    }
}
