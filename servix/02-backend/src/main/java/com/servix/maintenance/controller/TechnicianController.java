package com.servix.maintenance.controller;

import com.servix.maintenance.model.Technician;
import com.servix.maintenance.model.User;
import com.servix.maintenance.repository.TechnicianRepository;
import com.servix.maintenance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/technicians")
@RequiredArgsConstructor
public class TechnicianController {

    private final TechnicianRepository technicianRepository;
    private final UserRepository userRepository;

    @GetMapping
    public List<Technician> getAll() {
        return technicianRepository.findAll();
    }

    @PostMapping
    public Technician create(@RequestBody Technician technician) {
        return technicianRepository.save(technician);
    }
}

