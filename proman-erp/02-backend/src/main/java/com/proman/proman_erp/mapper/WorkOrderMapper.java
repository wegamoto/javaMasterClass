package com.proman.proman_erp.mapper;

import com.proman.proman_erp.dto.WorkOrderDTO;
import com.proman.proman_erp.entity.Product;
import com.proman.proman_erp.entity.WorkOrder;
import org.springframework.stereotype.Component;

@Component
public class WorkOrderMapper {

    public WorkOrderDTO toDTO(WorkOrder entity) {
        WorkOrderDTO dto = new WorkOrderDTO();
        dto.setId(entity.getId());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setProductId(entity.getProduct().getId());
        dto.setProductName(entity.getProduct().getName());
        dto.setQuantity(entity.getQuantity());
        dto.setScheduledDate(entity.getScheduledDate());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    public WorkOrder toEntity(WorkOrderDTO dto, Product product) {
        WorkOrder entity = new WorkOrder();
        entity.setId(dto.getId());
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setProduct(product);
        entity.setQuantity(dto.getQuantity());
        entity.setScheduledDate(dto.getScheduledDate());
        entity.setStatus(dto.getStatus());
        return entity;
    }
}
