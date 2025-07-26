package com.wewe.swiftaid.controller;

import com.wewe.swiftaid.entity.EmergencyCase;
import com.wewe.swiftaid.repository.AmbulanceTeamRepository;
import com.wewe.swiftaid.repository.EmergencyCaseRepository;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/emergencies")
public class EmergencyCaseController {

    private final EmergencyCaseRepository emergencyCaseRepository;
    private final AmbulanceTeamRepository ambulanceTeamRepository;

    public EmergencyCaseController(EmergencyCaseRepository emergencyCaseRepository,
                                   AmbulanceTeamRepository ambulanceTeamRepository) {
        this.emergencyCaseRepository = emergencyCaseRepository;
        this.ambulanceTeamRepository = ambulanceTeamRepository;
    }

    // แสดงรายการทั้งหมด
    @GetMapping
    public String list(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                       Model model) {

        List<EmergencyCase> cases;

        if (start != null && end != null) {
            LocalDateTime startDateTime = start.atStartOfDay();
            LocalDateTime endDateTime = end.atTime(LocalTime.MAX);
            cases = emergencyCaseRepository.findByReportedAtBetweenOrderByReportedAtDesc(startDateTime, endDateTime);
        } else {
            cases = emergencyCaseRepository.findAll(Sort.by(Sort.Direction.DESC, "reportedAt"));
        }

        model.addAttribute("cases", cases);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        return "emergency/list";
    }

    // ฟอร์มเพิ่มใหม่
    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("emergencyCase", new EmergencyCase());
        model.addAttribute("teams", ambulanceTeamRepository.findAll());
        return "emergency/form";
    }

    // บันทึกข้อมูลใหม่
    @PostMapping
    public String save(@ModelAttribute EmergencyCase emergencyCase) {
        emergencyCase.setReportedAt(LocalDateTime.now());
        emergencyCaseRepository.save(emergencyCase);
        return "redirect:/emergencies";
    }

    // แสดงรายละเอียด
    @GetMapping("/{id}")
    public String showDetail(@PathVariable Long id, Model model) {
        EmergencyCase ec = emergencyCaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid case Id: " + id));
        model.addAttribute("emergencyCase", ec);
        return "emergency/detail";
    }

    // ✅ แสดงฟอร์มแก้ไข
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        EmergencyCase emergencyCase = emergencyCaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ไม่พบเคส ID: " + id));
        model.addAttribute("emergencyCase", emergencyCase);
        model.addAttribute("teams", ambulanceTeamRepository.findAll());
        return "emergency/form";
    }

    // ✅ อัปเดตข้อมูล
    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute EmergencyCase updatedCase) {
        EmergencyCase existing = emergencyCaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ไม่พบเคส ID: " + id));

        existing.setDescription(updatedCase.getDescription());
        existing.setStatus(updatedCase.getStatus());
        existing.setTeam(updatedCase.getTeam());

        emergencyCaseRepository.save(existing);
        return "redirect:/emergencies";
    }

    // ✅ ลบเคส
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        emergencyCaseRepository.deleteById(id);
        return "redirect:/emergencies";
    }
}
