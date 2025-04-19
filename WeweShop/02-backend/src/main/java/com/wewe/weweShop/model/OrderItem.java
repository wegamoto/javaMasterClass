package com.wewe.weweShop.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id") // ✅ ใส่ที่ฝั่งนี้แทน
    private Order order;

    private String productName;
    private int quantity;
    private double price;
}
