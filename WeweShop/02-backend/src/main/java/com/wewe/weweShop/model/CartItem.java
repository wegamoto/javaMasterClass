package com.wewe.weweShop.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "cart_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(name = "product_id")
    private Long productId;

    private String productName;

    private BigDecimal price;

    private int quantity;

    private String userEmail; // เชื่อมกับผู้ใช้

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

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
