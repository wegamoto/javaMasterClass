package com.wewe.marketflow.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class BudgetItem {
    @Id
    @GeneratedValue
    private Long id;

    private String category; // เช่น MEDIA, PRINT, INFLUENCER, etc.
    private String description;
    private BigDecimal amount;

    private LocalDate spendDate;

    @ManyToOne
    private Campaign campaign;
}

