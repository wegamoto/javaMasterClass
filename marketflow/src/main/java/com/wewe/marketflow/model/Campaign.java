package com.wewe.marketflow.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "campaign")
@Data
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 255)
    private String type; // e.g., ONLINE, OFFLINE

    @Column(length = 255)
    private String status; // e.g., PLANNED, ONGOING, COMPLETED

    private LocalDate startDate;
    private LocalDate endDate;

    @Column(length = 2000)
    private String description;

    @Column(precision = 38, scale = 2)
    private BigDecimal estimatedBudget;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;
}
