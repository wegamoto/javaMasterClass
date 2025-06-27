package com.wewe.weweShop.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> items;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ===== Constructor =====
    public Cart() {
    }

    // ===== Getters & Setters =====
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // ===== Convenience Methods =====

    /**
     * ดึง username จาก user ที่ผูกกับ cart
     */
    public String getUsername() {
        return user != null ? user.getUsername() : null;
    }

    /**
     * ถ้าคุณต้องการให้ setUserId() ทำงานจริง ควรใช้แบบนี้:
     * (แต่ถ้าไม่ใช้ สามารถลบทิ้งได้เลย)
     */
    public void setUserId(Long userId) {
        if (this.user == null) {
            this.user = new User();
        }
        this.user.setId(userId);
    }
}
