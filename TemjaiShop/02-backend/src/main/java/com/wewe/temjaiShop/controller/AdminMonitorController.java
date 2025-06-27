package com.wewe.temjaiShop.controller;

import com.wewe.temjaiShop.dto.OrderWithBadge;
import com.wewe.temjaiShop.model.Order;
import com.wewe.temjaiShop.repository.OrderRepository;
import com.wewe.temjaiShop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminMonitorController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders-monitor")
    public String viewAllOrders(
            @RequestParam(value = "page", required = false, defaultValue = "0") String pageStr,
            Model model) {

        int page = 0;
        try {
            if (pageStr != null && !pageStr.isBlank()) {
                page = Integer.parseInt(pageStr);
            }
        } catch (NumberFormatException e) {
            page = 0; // fallback หากพารามิเตอร์ผิด
        }

        // TODO: เพิ่ม pagination ถ้าต้องการ
        List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));

        List<OrderWithBadge> ordersWithBadge = orders.stream()
                .map(order -> new OrderWithBadge(order, getStatusBadgeClass(order.getStatus())))
                .toList();

        // ฟอร์แมตราคาตรงนี้ แล้วเก็บไว้ใน DTO (OrderWithBadge) เลย
        ordersWithBadge.forEach(orderWithBadge -> {
            BigDecimal total = orderWithBadge.getOrder().getTotalAmount();
            String totalFormatted = String.format("%,.2f ฿", total);
            orderWithBadge.setTotalFormatted(totalFormatted);
        });

        model.addAttribute("orders", ordersWithBadge);
        model.addAttribute("page", page); // เผื่อใช้แสดง pagination

        return "admin-orders";
    }


    private String getStatusBadgeClass(Order.Status status) {
        if (status == null) return "bg-light text-dark";

        return switch (status) {
            case CREATED -> "bg-info text-dark";
            case WAITING_FOR_PAYMENT, PENDING_PAYMENT -> "bg-warning text-dark";
            case PENDING_VERIFICATION -> "bg-secondary";
            case PAID -> "bg-primary";
            case COMPLETED -> "bg-success";
            case CANCELLED -> "bg-danger";
            default -> "bg-light text-dark"; // ป้องกัน enum ใหม่ที่ยังไม่รองรับ
        };
    }

    @GetMapping("/orders/{id}")
    public String viewOrderDetails(@PathVariable Long id, Model model) {
        Order order = orderService.findById(id);
        OrderWithBadge dto = new OrderWithBadge(order, getStatusBadgeClass(order.getStatus()));

        // Format ค่า ใช้ TotalAmount
        dto.setTotalFormatted(formatCurrency(order.getTotalAmount()));// ex. "1,250.00 ฿"

        model.addAttribute("order", dto); // ส่ง DTO แทน Entity
        return "admin-order-details";
    }

    private String formatCurrency(BigDecimal amount) {
        if (amount == null) {
            return "0.00 ฿";
        }

        NumberFormat currencyFormat = NumberFormat.getNumberInstance(new Locale("th", "TH"));
        currencyFormat.setMinimumFractionDigits(2);
        currencyFormat.setMaximumFractionDigits(2);
        return currencyFormat.format(amount) + " ฿";
    }

}

