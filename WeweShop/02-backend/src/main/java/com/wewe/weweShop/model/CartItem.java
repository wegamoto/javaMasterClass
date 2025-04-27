package com.wewe.weweShop.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_email", nullable = false)
    private String userEmail; // เก็บ email ของผู้ใช้งานตะกร้านี้ (ไม่ต้อง join user ตรงๆ ก็ได้)

    private Long userId;

    @Column(name = "product_id", nullable = false)
    private Long productId; // ID ของสินค้าที่หยิบใส่ตะกร้า

    @Column(name = "product_name", nullable = false)
    private String productName; // ชื่อสินค้า (เผื่อสินค้าเปลี่ยนชื่อภายหลัง)

    @Column(name = "price", nullable = false)
    private BigDecimal price; // ราคาต่อหน่วยของสินค้า ณ ตอนหยิบใส่ตะกร้า

    @Column(name = "quantity", nullable = false)
    private Integer quantity; // จำนวนสินค้าที่หยิบใส่ตะกร้า

    @ManyToOne(fetch = FetchType.LAZY) // โหลดเฉพาะตอนที่จำเป็น ไม่หน่วงระบบ
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;  // สำหรับดึงข้อมูลสินค้าเพิ่มเติมเวลาแสดงตะกร้า

    /**
     * คำนวณราคารวมต่อรายการในตะกร้า (price * quantity)
     */
    @Transient
    public BigDecimal getTotal() {
        if (price != null && quantity > 0) {
            return price.multiply(BigDecimal.valueOf(quantity));
        }
        return BigDecimal.ZERO;
    }

    // equals & hashCode สำหรับเช็คว่าเป็นสินค้ารายการเดิมไหม
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItem)) return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equals(productId, cartItem.productId) &&
                Objects.equals(userEmail, cartItem.userEmail); // เพิ่ม userEmail เพื่อแยก user
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, userEmail);
    }

    public void setTotal(BigDecimal total) {
    }

}
