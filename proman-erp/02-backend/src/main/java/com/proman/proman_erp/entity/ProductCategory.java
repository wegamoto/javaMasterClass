package com.proman.proman_erp.entity;

import jakarta.persistence.*;
import lombok.Data;

// ProductCategory.java
@Entity
@Table(name = "product_categories")
@Data
public class ProductCategory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;
}
