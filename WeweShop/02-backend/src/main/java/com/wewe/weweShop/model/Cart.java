package com.wewe.weweShop.model;

import com.wewe.weweShop.model.CartItem;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(nullable = false)
//    private Long userId; // ✅ ต้องมี field userId แบบนี้ชัดเจน

    @Getter
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> items;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Assuming you have a User entity and it's linked with Cart

    public String getUsername() {
        return user != null ? user.getUsername() : null;
    }

    public void setUserId(Long userId) {
    }

    public void setUser(User user) {

    }

    // getter/setter
}

