package com.wewe.marketflow.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class Campaign {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String type; // ONLINE, OFFLINE
    private String status; // PLANNED, ONGOING, COMPLETED

    private LocalDate startDate;
    private LocalDate endDate;

    @Column(length = 2000)
    private String description;

    @ManyToOne
    private User owner; // ผู้รับผิดชอบ

    private BigDecimal estimatedBudget;
}

