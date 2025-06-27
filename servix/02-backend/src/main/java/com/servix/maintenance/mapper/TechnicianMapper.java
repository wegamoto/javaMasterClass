package com.servix.maintenance.mapper;

import com.servix.maintenance.dto.TechnicianDTO;
import com.servix.maintenance.model.Technician;
import org.springframework.stereotype.Component;

@Component
public class TechnicianMapper {

    public TechnicianDTO toDto(Technician technician) {
        TechnicianDTO dto = new TechnicianDTO();
        dto.setId(technician.getId());
        dto.setName(technician.getName());
        dto.setSkillLevel(technician.getSkillLevel() != null ? technician.getSkillLevel().name() : null);
        return dto;
    }
}

