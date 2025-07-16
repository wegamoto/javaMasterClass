package com.wewe.marketflow.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "app_setting")
@Data
public class AppSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String key;

    @Column(columnDefinition = "TEXT")
    private String value;

    private String description; // อธิบายว่าค่าตั้งค่านี้ใช้ทำอะไร
}

