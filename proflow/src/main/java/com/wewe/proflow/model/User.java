package com.wewe.proflow.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // เช่น ADMIN, CONTRACTOR, FINANCE

    private String password; // เข้ารหัสด้วย BCrypt

    @OneToMany(mappedBy = "contractor")
    private List<CashFlow> entries;
}

