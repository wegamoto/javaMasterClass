package com.servix.maintenance.mapper;

import com.servix.maintenance.dto.WorkOrderDTO;
import com.servix.maintenance.model.WorkOrder;
import org.springframework.stereotype.Component;

@Component
public class WorkOrderMapper {

    public WorkOrderDTO toDto(WorkOrder workOrder) {
        WorkOrderDTO dto = new WorkOrderDTO();
        dto.setId(workOrder.getId());
        dto.setEquipmentName(workOrder.getEquipmentName());
        dto.setDescription(workOrder.getDescription());
        dto.setStatus(workOrder.getStatus() != null ? workOrder.getStatus().name() : null);
        dto.setCompletedAt(workOrder.getCompletedAt());
        dto.setCreatedAt(workOrder.getCreatedAt());

        return dto;
    }
}
