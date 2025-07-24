package com.wewe.taxcertify.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class WithholdingTaxForm {

    private Long id;          // <-- เพิ่ม id
    private Long payerId;
    private Long payeeId;
    private LocalDate date;
    private BigDecimal amount;
    private BigDecimal taxRate;
    private String description;

    // Constructors
    public WithholdingTaxForm() {
    }

    public WithholdingTaxForm(Long id, Long payerId, LocalDate date,
                              BigDecimal amount, BigDecimal taxRate, String description) {
        this.id = id;
        this.payerId = payerId;
        this.payeeId = payeeId;
        this.date = date;
        this.amount = amount;
        this.taxRate = taxRate;
        this.description = description;
    }

    // Getter & Setter for id
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    // Existing getters/setters...

    public Long getPayerId() { return payerId; }
    public void setPayerId(Long payerId) { this.payerId = payerId; }

    public Long getPayeeId() { return payeeId; }
    public void setPayeeId(Long payeeId) { this.payeeId = payeeId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getTaxRate() { return taxRate; }
    public void setTaxRate(BigDecimal taxRate) { this.taxRate = taxRate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "WithholdingTaxForm{" +
                "id=" + id +
                ", payerId=" + payerId +
                ", payeeId=" + payeeId +
                ", date=" + date +
                ", amount=" + amount +
                ", taxRate=" + taxRate +
                ", description='" + description + '\'' +
                '}';
    }

    public String getFormattedDate() {
        return this.date != null
                ? this.date.format(DateTimeFormatter.ofPattern("dd MMM yyyy", new Locale("th", "TH")))
                : "";
    }
}
