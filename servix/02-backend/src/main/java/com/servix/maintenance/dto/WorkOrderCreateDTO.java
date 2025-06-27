package com.servix.maintenance.dto;

import com.servix.maintenance.model.WorkOrderStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WorkOrderCreateDTO {
    @NotBlank
    private String description;

    private WorkOrderStatus status; // Optional: default = OPEN (handled in service)
}

