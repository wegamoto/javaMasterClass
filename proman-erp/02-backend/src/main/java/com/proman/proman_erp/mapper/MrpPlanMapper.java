package com.proman.proman_erp.mapper;

import com.proman.proman_erp.dto.MrpPlanDTO;
import com.proman.proman_erp.entity.MrpPlan;
import org.springframework.stereotype.Component;

@Component
public class MrpPlanMapper {
    public MrpPlanDTO toDTO(MrpPlan entity) {
        MrpPlanDTO dto = new MrpPlanDTO();
        dto.setId(entity.getId());
        dto.setPlanningDate(entity.getPlanningDate());
        dto.setProductId(entity.getProduct().getId());
        dto.setProductName(entity.getProduct().getName());
        dto.setRequiredQuantity(entity.getRequiredQuantity());
        dto.setAvailableQuantity(entity.getAvailableQuantity());
        dto.setToProduceQuantity(entity.getToProduceQuantity());
        return dto;
    }
}
