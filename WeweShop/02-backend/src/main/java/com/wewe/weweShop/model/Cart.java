package com.wewe.weweShop.model;

import com.wewe.weweShop.model.CartItem;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId; // ✅ ต้องมี field userId แบบนี้ชัดเจน

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> items;

    public void setUserId(Long userId) {
    }

    // getter/setter
}

