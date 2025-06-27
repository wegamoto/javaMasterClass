package com.servix.maintenance.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class WorkOrderDTO {
    private Long id;
    private String equipmentName;
    private String description;
    private String status;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
}

