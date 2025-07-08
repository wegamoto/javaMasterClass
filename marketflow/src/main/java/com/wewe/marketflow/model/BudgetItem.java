package com.wewe.marketflow.model;

@Entity
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

