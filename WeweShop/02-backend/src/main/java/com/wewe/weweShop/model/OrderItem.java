package com.wewe.weweShop.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many OrderItems จะอ้างถึง Order เดียวกัน
    @ManyToOne(fetch = FetchType.LAZY) // ดึงข้อมูล Order ตอนจำเป็น
    @JoinColumn(name = "order_id") // ✅ ใส่ที่ฝั่งนี้แทน
    private Order order;

    @Column(name = "product_id", nullable = false)
    private Long productId; // ไว้อ้างอิงสินค้า

    @Column(name = "product_name", nullable = false)
    private String productName; // ชื่อสินค้า (copy มาเวลาสั่งซื้อ เพื่อกันข้อมูลเปลี่ยนทีหลัง)

    @Column(name = "quantity", nullable = false)
    private int quantity;  // จำนวนสินค้าที่ซื้อ

    @Column(name = "price", nullable = false)
    private BigDecimal price; // ราคาต่อหน่วย ณ ตอนซื้อ

    @Column(name = "total", nullable = false)
    private BigDecimal total; // ราคารวมของ (price * quantity)

    // Optional: ถ้าอยาก map ไปที่ Product ด้วย (ไม่จำเป็น)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    @PrePersist
    public void prePersist() {
        if (total == null) {
            this.total = price.multiply(BigDecimal.valueOf(quantity));
        }
    }
}
