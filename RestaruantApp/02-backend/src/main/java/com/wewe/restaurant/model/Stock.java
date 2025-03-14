package com.wewe.restaurant.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Entity
@Getter
@Setter
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // รหัสของสต๊อก

    private String name; // ชื่อของวัตถุดิบ
    private double quantity; // ปริมาณในสต๊อก (เช่น กิโลกรัม, ลิตร, ชิ้น)
    private double price; // ราคาต่อหน่วย

    @ManyToOne
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem; // การเชื่อมโยงกับ MenuItem เพื่อใช้งานในเมนูต่างๆ

    public Stock() {
    }

    public Stock(String name, double quantity, double price, MenuItem menuItem) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.menuItem = menuItem;
    }
}

