package com.wewe.taxcertify.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import com.wewe.taxcertify.model.Payer;
import com.wewe.taxcertify.model.Payee;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WithholdingTaxRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // getters and setters
    @ManyToOne
    @JoinColumn(name = "payer_id")
    private Payer payer;

    @ManyToOne
    @JoinColumn(name = "payee_id")
    private Payee payee;

    private LocalDate date;

    private String incomeType;      // ประเภทเงินได้

    @Builder.Default
    private Integer taxFormSequence = 0; // ลำดับที่แบบภาษี

    private BigDecimal amount;        // จำนวนเงินจ่าย
    private BigDecimal taxRate;       // เช่น 0.03 (3%)
    private BigDecimal taxAmount;     // จำนวนภาษีที่หัก
    private String description;       // รายละเอียด เช่น "ค่าทำระบบ"

    @PrePersist
    public void calculateTax() {
        if (amount != null && taxRate != null) {
            this.taxAmount = amount.multiply(taxRate);
        }
    }

    public BigDecimal getTaxAmount() {
        if (amount == null || taxRate == null) {
            return BigDecimal.ZERO;
        }
        return amount.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
    }

}

