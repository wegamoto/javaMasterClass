package com.wewe.restaurant.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
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

    @ManyToOne
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @Column(name = "order_date", nullable = false)  // ✅ ใช้ @Column ได้
    private LocalDateTime orderDate;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "quantity", nullable = false)  // ✅ ใช้ @Column แทน
    private int quantity;

    @Column(name = "totalPrice", nullable = false)  // ✅ ใช้ @Column แทน
    private double totalPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "order_id") // เชื่อมโยงกับรายการเมนูในคำสั่งซื้อ
    private List<MenuItem> menuItems; // รายการเมนูในคำสั่งซื้อ

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer; // ฟิลด์ Customer ที่เชื่อมโยงกับ Order ลูกค้าที่ทำการสั่งซื้อ

    // ✅ เพิ่ม Getter และ Setter


    public Order() {
        this.orderDate = LocalDateTime.parse(String.valueOf(LocalDateTime.now())); // ค่าเริ่มต้นเป็นเวลาปัจจุบัน
        this.status = String.valueOf(OrderStatus.PENDING);
    }

}

