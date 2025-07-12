package com.wewe.marketflow.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Data
public class UserRole implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 255)
    private String description; // 👉 เพิ่ม field นี้เพื่อเก็บคำอธิบาย

    @Override
    public String getAuthority() {
        return name;
    }
}
