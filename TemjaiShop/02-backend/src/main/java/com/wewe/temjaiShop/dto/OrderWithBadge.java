package com.wewe.temjaiShop.dto;

import com.wewe.temjaiShop.model.Order;

import com.wewe.temjaiShop.model.Order;
import com.wewe.temjaiShop.model.OrderItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderWithBadge {

    private final Order order;
    private final String badgeClass;
    private String totalFormatted;

    public OrderWithBadge(Order order, String badgeClass) {
        this.order = order;
        this.badgeClass = badgeClass;
    }

    public Long getId() {
        return order.getId();
    }

    public String getCustomerName() {
        return order.getCustomerName();
    }

    public Order.Status getStatus() {
        return order.getStatus();
    }

    public BigDecimal getTotal() {
        return order.getTotalAmount() != null ? order.getTotalAmount() : BigDecimal.ZERO;
    }

    public LocalDateTime getCreatedAt() {
        return order.getCreatedAt();
    }

    public String getBadgeClass() {
        return badgeClass;
    }

    // หรือจะ expose ทั้ง Order object ไปเลยก็ได้ ถ้าต้องการ:
    public Order getOrder() {
        return order;
    }

    public String getTotalFormatted() {
        return totalFormatted;
    }

    public void setTotalFormatted(String totalFormatted) {
        this.totalFormatted = totalFormatted;
    }

    public String getCreatedAtFormatted() {
        if (order.getCreatedAt() == null) return "";
        return order.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    // ✅ เพิ่มเมธอดนี้
    public List<OrderItem> getOrderItems() {
        return order.getOrderItems();
    }
}


