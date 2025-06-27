package com.wewe.temjaiShop.dto;

import com.wewe.temjaiShop.model.Order;
import com.wewe.temjaiShop.model.Order.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderWithBadgeDTO {

    private Long id;
    private Status status;
    private String badgeClass;
    private BigDecimal totalAmount;
    private String totalFormatted;
    private LocalDateTime createdAt;

    public OrderWithBadgeDTO(Order order, String badgeClass, String totalFormatted) {
        this.id = order.getId();
        this.status = order.getStatus();
        this.badgeClass = badgeClass;
        this.totalAmount = order.getTotalAmount();
        this.totalFormatted = String.format("%,.2f ฿", order.getTotalAmount());
        this.createdAt = order.getCreatedAt();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getBadgeClass() {
        return badgeClass;
    }

    public void setBadgeClass(String badgeClass) {
        this.badgeClass = badgeClass;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalFormatted() {
        return totalFormatted;
    }

    public void setTotalFormatted(String totalFormatted) {
        this.totalFormatted = totalFormatted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
