package com.wewe.temjaiShop.controller;

import com.wewe.temjaiShop.dto.OrderWithBadgeDTO;
import com.wewe.temjaiShop.model.Order;
import com.wewe.temjaiShop.model.Order.Status;
import com.wewe.temjaiShop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class OrderMonitorController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/orders-monitor")
    public Map<String, Object> getOrdersMonitor(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            Authentication authentication
    ) {
        // ✅ Get orders with pagination
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Order> ordersPage = orderRepository.findAll(pageable);

        List<OrderWithBadgeDTO> orders = ordersPage.getContent().stream()
                .map(order -> {
                    String badge = getStatusBadgeClass(order.getStatus());
                    String totalFormatted = formatAmount(order.getTotalAmount());
                    return new OrderWithBadgeDTO(order, badge, totalFormatted);
                })
                .toList();

        // ✅ Extract JWT (if token stored in credentials by your JWT filter)
        String token = (authentication.getCredentials() != null)
                ? authentication.getCredentials().toString()
                : "";

        // ✅ Extract username and roles
        String username = authentication.getName();
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // ✅ Create response
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("username", username);
        response.put("roles", roles);
        response.put("orders", orders);

        return response;
    }

    private String getStatusBadgeClass(Status status) {
        return switch (status) {
            case PENDING_PAYMENT, PENDING_VERIFICATION, WAITING_FOR_PAYMENT -> "badge-warning";
            case PAID, COMPLETED -> "badge-success";
            case CANCELLED -> "badge-danger";
            case CREATED -> "badge-secondary";
        };
    }

    private String formatAmount(BigDecimal amount) {
        if (amount == null) return "0.00 ฿";
        return String.format("%,.2f ฿", amount);
    }
}
