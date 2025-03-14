package com.wewe.restaurant.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // รหัสหมวดหมู่

    @Column(nullable = false,unique = true)
    private String name; // ชื่อหมวดหมู่

    private String description;

    @OneToMany(mappedBy = "category") // หมวดหมู่สามารถมีเมนูหลายรายการ
    private List<MenuItem> menuItems; // เมนูทั้งหมดในหมวดหมู่

    public Category() {}

    public Category(String name) {
        this.name = name;
    }
}

