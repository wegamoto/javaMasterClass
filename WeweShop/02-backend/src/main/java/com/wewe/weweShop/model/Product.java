package com.wewe.weweShop.model;

import jakarta.persistence.*;

import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double price;
    private int stock;
    private int quantity;

    private String image;

    private boolean onSale;       // << เพิ่ม
    private boolean bestSeller;   // << เพิ่ม

    @Column(name = "created_at")
    private LocalDateTime createdAt;


    // 🔥 ต้องมี @ManyToOne หรือ Join กับ Category ด้วย
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

