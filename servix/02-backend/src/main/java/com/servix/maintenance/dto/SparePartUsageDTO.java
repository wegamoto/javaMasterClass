package com.servix.maintenance.dto;

import lombok.Data;

@Data
public class SparePartUsageDTO {
    private Long id;
    private Long workOrderId;
    private Long sparePartId;
    private String sparePartName;
    private int quantityUsed;
}

