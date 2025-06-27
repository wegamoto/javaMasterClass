package com.servix.maintenance.service;

import com.servix.maintenance.model.Technician;
import com.servix.maintenance.repository.TechnicianRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TechnicianService {

    private final TechnicianRepository technicianRepository;

    public List<Technician> getAllTechnicians() {
        return technicianRepository.findAll();
    }

    public Technician getTechnicianById(Long id) {
        return technicianRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Technician not found"));
    }

    public Technician createTechnician(Technician technician) {
        return technicianRepository.save(technician);
    }

    public Technician save(Technician tech) {
        return technicianRepository.save(tech);
    }

    public Technician updateTechnician(Long id, Technician updatedTechnician) {
        Technician existing = getTechnicianById(id);
        existing.setName(updatedTechnician.getName());
        existing.setPhone(updatedTechnician.getPhone());
        existing.setFullName(updatedTechnician.getFullName());
        return technicianRepository.save(existing);
    }

    public void deleteTechnician(Long id) {
        if (!technicianRepository.existsById(id)) {
            throw new RuntimeException("Technician not found");
        }
        technicianRepository.deleteById(id);
    }
}
