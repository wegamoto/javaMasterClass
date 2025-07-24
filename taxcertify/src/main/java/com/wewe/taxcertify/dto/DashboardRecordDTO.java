package com.wewe.taxcertify.dto;

public class DashboardRecordDTO {
    private Long id;
    private String payerName;
    private String payeeName;
    private String formattedDate;
    private String formattedAmount;
    private String formattedTaxAmount;
    private String description;

    public DashboardRecordDTO(Long id, String payerName, String payeeName, String formattedDate,
                              String formattedAmount, String formattedTaxAmount, String description) {
        this.id = id;
        this.payerName = payerName;
        this.payeeName = payeeName;
        this.formattedDate = formattedDate;
        this.formattedAmount = formattedAmount;
        this.formattedTaxAmount = formattedTaxAmount;
        this.description = description;
    }

    // Getters เท่านั้นพอสำหรับ Thymeleaf
    public Long getId() {
        return id;
    }

    public String getPayerName() {
        return payerName;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public String getFormattedAmount() {
        return formattedAmount;
    }

    public String getFormattedTaxAmount() {
        return formattedTaxAmount;
    }

    public String getDescription() {
        return description;
    }
}

