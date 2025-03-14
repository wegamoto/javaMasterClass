package com.wewe.restaurant.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // ต้องมี user_id เสมอ
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "order_id") // เชื่อมโยงกับรายการเมนูในคำสั่งซื้อ
    private List<MenuItem> menuItems; // รายการเมนูในคำสั่งซื้อ

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer; // ฟิลด์ Customer ที่เชื่อมโยงกับ Order ลูกค้าที่ทำการสั่งซื้อ

    @ManyToOne
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;

    private String customerName;

    private String orderDate; // วันที่ทำการสั่งซื้อ

    private int quantity;

    private double totalPrice;

    public Order() {}

//    private String status;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    private LocalDateTime orderTime = LocalDateTime.now();

    public Order(String orderDate, double totalPrice, Customer customer, List<MenuItem> menuItems) {
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.customer = customer;
        this.menuItems = menuItems;
    }

    // Getters & Setters


}

