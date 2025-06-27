package com.servix.maintenance.dto;

import com.servix.maintenance.model.WorkOrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkOrderUpdateDTO {
    private String description;
    private WorkOrderStatus status;
    private LocalDateTime completedAt;
}

