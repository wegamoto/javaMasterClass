package com.servix.maintenance.mapper;

import com.servix.maintenance.dto.SparePartDTO;
import com.servix.maintenance.model.SparePart;
import org.springframework.stereotype.Component;

@Component
public class SparePartMapper {

    public SparePartDTO toDto(SparePart sparePart) {
        SparePartDTO dto = new SparePartDTO();
        dto.setId(sparePart.getId());
        dto.setName(sparePart.getName());
        dto.setQuantityInStock(sparePart.getQuantityInStock());
        return dto;
    }
}

