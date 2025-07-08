package com.wewe.proflow.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CashFlowDTO {
    private Long id;
    private LocalDate date;
    private Double amount;
    private String type;        // "INCOME" หรือ "EXPENSE"
    private String description;

    private Long projectId;
    private Long phaseId;
    private Long contractorId;
}

