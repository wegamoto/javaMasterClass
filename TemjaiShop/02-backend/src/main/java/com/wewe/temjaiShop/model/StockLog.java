package com.wewe.temjaiShop.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class StockLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    @Column(name = "quantity_change")
    private Integer quantityChange;

    private String action;

    private LocalDateTime timestamp;

    // ===== Lifecycle Callback =====
    @PrePersist
    public void onCreate() {
        timestamp = LocalDateTime.now();
    }

    // ===== Getters and Setters =====
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getChange() {
        return quantityChange;
    }

    public void setChange(int change) {
        this.quantityChange = change;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "StockLog{" +
                "id=" + id +
                ", productId=" + productId +
                ", change=" + quantityChange +
                ", action='" + action + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
