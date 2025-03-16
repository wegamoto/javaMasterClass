package com.wewe.restaurant.model;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = false, unique = true)
    private String username;

    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role; // CUSTOMER, ADMIN, STAFF

    // ความสัมพันธ์กับร้านอาหาร
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = true) // nulable ได้ในกรณี user ไม่ได้มีร้านอาหาร
    private Restaurant restaurant;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;
}

