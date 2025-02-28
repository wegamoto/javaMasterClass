package com.wewe.crudapi.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal totalInvestment = BigDecimal.ZERO;

    @OneToOne(mappedBy = "wallet")
    private Investor investor;

    // Getter & Setter
}

