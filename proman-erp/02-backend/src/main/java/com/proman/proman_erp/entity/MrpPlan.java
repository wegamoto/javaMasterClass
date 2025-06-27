package com.proman.proman_erp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "mrp_plans")
@Data
public class MrpPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime planningDate;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer requiredQuantity;

    private Integer availableQuantity;

    private Integer toProduceQuantity;
}

