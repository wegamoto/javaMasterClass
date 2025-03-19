package com.wewe.restaurant.model;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)  // บังคับให้ไม่เป็น null
    private String name;  // ชื่อที่ต้องมีค่า

    @Column(nullable = false, unique = true)
    private String username;

    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "role_id")
    private Role role; // CUSTOMER, ADMIN, STAFF

    // ความสัมพันธ์กับร้านอาหาร
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = true) // nulable ได้ในกรณี user ไม่ได้มีร้านอาหาร
    private Restaurant restaurant;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;

    // constructor ที่รับค่า name
    public User(String name, String username, String password, String email, Role role) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }
}

