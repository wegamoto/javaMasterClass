package com.wewe.restaurant.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // รหัสหมวดหมู่

    @Column(nullable = false,unique = true)
    private String name; // ชื่อหมวดหมู่

    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // หมวดหมู่สามารถมีเมนูหลายรายการ
    private Set<MenuItem> menuItems = new HashSet<>(); // เมนูทั้งหมดในหมวดหมู่

    public Category() {}

    public Category(String name) {
        this.name = name;
    }
}

