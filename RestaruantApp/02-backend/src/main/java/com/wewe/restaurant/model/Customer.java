package com.wewe.restaurant.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;

    @OneToMany(mappedBy = "customer")  // ความสัมพันธ์ One-to-Many กับ Order
    private List<Order> orders; // รายการคำสั่งซื้อของลูกค้า

    public Customer() {
    }

    public Customer(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // เพิ่ม getters และ setters สำหรับ fields ตามต้องการ
}

