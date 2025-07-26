package com.wewe.swiftaid.controller;

import com.wewe.swiftaid.entity.AmbulanceTeam;
import com.wewe.swiftaid.repository.AmbulanceTeamRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/teams")
public class AmbulanceTeamController {

    private final AmbulanceTeamRepository ambulanceTeamRepository;

    public AmbulanceTeamController(AmbulanceTeamRepository ambulanceTeamRepository) {
        this.ambulanceTeamRepository = ambulanceTeamRepository;
    }

    @GetMapping
    public String listTeams(Model model) {
        model.addAttribute("teams", ambulanceTeamRepository.findAll());
        return "team/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("team", new AmbulanceTeam());
        return "team/form";
    }

    @PostMapping
    public String saveTeam(@ModelAttribute("team") AmbulanceTeam team) {
        ambulanceTeamRepository.save(team);
        return "redirect:/teams";
    }

    @GetMapping("/{id}/edit")
    public String editTeam(@PathVariable Long id, Model model) {
        AmbulanceTeam team = ambulanceTeamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid team ID: " + id));
        model.addAttribute("team", team);
        return "team/edit";
    }

    @PostMapping("/{id}")
    public String updateTeam(@PathVariable Long id, @ModelAttribute AmbulanceTeam team) {
        team.setId(id); // เผื่อไว้ในกรณีที่ id หาย
        ambulanceTeamRepository.save(team);
        return "redirect:/teams";
    }
}

