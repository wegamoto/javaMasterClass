package com.wewe.taxcertify.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;  // ควรเข้ารหัสด้วย BCrypt

    private String role; // เช่น ADMIN, USER
}

