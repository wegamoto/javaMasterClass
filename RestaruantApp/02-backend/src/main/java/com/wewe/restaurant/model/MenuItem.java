package com.wewe.restaurant.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "menu_items")  // กำหนดชื่อของตารางในฐานข้อมูล
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // รหัสเมนู

    @Column(nullable = false)
    private String name; // ชื่อเมนู

    @Column(nullable = false)
    private double price; // ราคาเมนู

    @Column(length = 500)  // กำหนดความยาวสูงสุด 500 ตัวอักษร
    private String description;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false) // เชื่อมกับตาราง restaurant
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false) // เชื่อมโยงกับประเภทของเมนู
    private Category category; // หมวดหมู่ของเมนู

    public MenuItem() {
        // Default constructor
    }

    public MenuItem(String name, String description, Double price, Category category, Restaurant restaurant) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.restaurant = restaurant;
    }
}

