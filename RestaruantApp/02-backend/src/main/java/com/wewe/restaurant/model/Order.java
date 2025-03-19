package com.wewe.restaurant.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // ต้องมี user_id เสมอ
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<OrderItem> orderItems = new HashSet<>(); // เปลี่ยนจาก List เป็น Set

    @Column(name = "order_date", nullable = false, updatable = false)  // ✅ ใช้ @Column ได้
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "quantity", nullable = false)  // ✅ ใช้ @Column แทน
    private int quantity;

    @Column(name = "totalPrice", nullable = false)  // ✅ ใช้ @Column แทน
    private double totalPrice;

    // ✅ คำนวณราคาเมื่อสร้างออเดอร์
    public void calculateTotalPrice() {
        this.totalPrice = orderItems.stream()
                .mapToDouble(orderItem -> orderItem.getMenuItem().getPrice() * orderItem.getQuantity())
                .sum();
    }

    @OneToMany
    @JoinColumn(name = "order_id") // เชื่อมโยงกับรายการเมนูในคำสั่งซื้อ
    private List<MenuItem> menuItems; // รายการเมนูในคำสั่งซื้อ

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer; // ฟิลด์ Customer ที่เชื่อมโยงกับ Order ลูกค้าที่ทำการสั่งซื้อ

    @PrePersist
    protected void onCreate() {
        this.orderDate = LocalDateTime.now();
    }

    public Order() {}

    // ✅ เพิ่ม Getter และ Setter

    public Order(User user, String status, Set<OrderItem> orderItems) {
        this.orderDate = LocalDateTime.now(); // ตั้งค่าเป็นเวลาปัจจุบันโดยตรง
        this.user = user;
        this.status = OrderStatus.valueOf(status.toUpperCase()); // แปลง String เป็น Enum
        this.orderItems = orderItems;
    }

}

