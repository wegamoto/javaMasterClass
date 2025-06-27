package com.wewe.temjaiShop.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(
        name = "cart_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"}),
        indexes = {
                @Index(columnList = "user_id"),
                @Index(columnList = "product_id")
        }
)
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // ❌ Lombok ถูกลบออกแล้ว —> เราสร้าง constructor เอง
    public CartItem() {
    }

    public CartItem(User user, Product product, String productName, BigDecimal price, Integer quantity) {
        this.user = user;
        this.product = product;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    // ✅ Getter และ Setter ทุกฟิลด์ (สามารถใช้ Generate ของ IDE ช่วยได้)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return  product !=null ? product.getId() : null;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Transient
    public BigDecimal getTotal() {
        if (price != null && quantity != null && quantity > 0) {
            return price.multiply(BigDecimal.valueOf(quantity));
        }
        return BigDecimal.ZERO;
    }

    // equals() และ hashCode() โดยใช้ id ถ้ามี ไม่งั้นใช้ user + product

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItem)) return false;
        CartItem that = (CartItem) o;
        return id != null ? id.equals(that.id) :
                (user != null && product != null &&
                        user.equals(that.user) &&
                        product.equals(that.product));
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() :
                (user != null && product != null ? user.hashCode() + product.hashCode() : 0);
    }
}
