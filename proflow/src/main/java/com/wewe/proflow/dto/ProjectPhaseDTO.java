package com.wewe.proflow.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectPhaseDTO {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long projectId; // เชื่อมกับ Project
}

