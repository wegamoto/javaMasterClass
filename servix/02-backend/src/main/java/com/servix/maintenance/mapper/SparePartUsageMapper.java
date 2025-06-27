package com.servix.maintenance.mapper;

import com.servix.maintenance.dto.SparePartUsageDTO;
import com.servix.maintenance.model.SparePartUsage;
import org.springframework.stereotype.Component;

@Component
public class SparePartUsageMapper {

    public SparePartUsageDTO toDto(SparePartUsage usage) {
        SparePartUsageDTO dto = new SparePartUsageDTO();
        dto.setId(usage.getId());
        dto.setWorkOrderId(usage.getWorkOrder().getId());
        dto.setSparePartId(usage.getSparePart().getId());
        dto.setSparePartName(usage.getSparePart().getName());
        dto.setQuantityUsed(usage.getQuantityUsed());
        return dto;
    }
}

