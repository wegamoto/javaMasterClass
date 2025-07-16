package com.wewe.marketflow.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_setting")
@Data
public class UserSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @Column(nullable = false)
    private String key;

    @Column(columnDefinition = "TEXT")
    private String value;
}

