package com.wewe.weweShop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "categories")
public class Category {

    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // ชื่อหมวดหมู่สินค้า

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Product> products; // รายการสินค้าที่อยู่ในหมวดหมู่นี้

    // Constructors
    public Category() {}

    public Category(String name) {
        this.name = name;
    }

}

