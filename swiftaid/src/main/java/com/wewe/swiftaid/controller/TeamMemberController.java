package com.wewe.swiftaid.controller;

import com.wewe.swiftaid.entity.TeamMember;
import com.wewe.swiftaid.repository.AmbulanceTeamRepository;
import com.wewe.swiftaid.repository.TeamMemberRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/team-members")
public class TeamMemberController {

    private final TeamMemberRepository teamMemberRepository;

    private final AmbulanceTeamRepository ambulanceTeamRepository;

    public TeamMemberController(TeamMemberRepository teamMemberRepository, AmbulanceTeamRepository ambulanceTeamRepository) {
        this.teamMemberRepository = teamMemberRepository;
        this.ambulanceTeamRepository = ambulanceTeamRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("members", teamMemberRepository.findAll());
        return "team-member/list";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("teamMember", new TeamMember());
        model.addAttribute("teams", ambulanceTeamRepository.findAll());
        return "team-member/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute TeamMember teamMember,
                       BindingResult result,
                       Model model) {
        if (result.hasErrors()) {
            model.addAttribute("teams", ambulanceTeamRepository.findAll());
            return "team-member/form";
        }
        teamMemberRepository.save(teamMember);
        return "redirect:/team-members";
    }

    @GetMapping("/{id}/edit") // ✅ ใช้ path ที่ถูกต้อง ไม่ซ้ำ
    public String showEditForm(@PathVariable Long id, Model model) {
        TeamMember member = teamMemberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ไม่พบสมาชิก ID: " + id));
        model.addAttribute("teamMember", member);
        model.addAttribute("teams", ambulanceTeamRepository.findAll());
        return "team-member/form"; // ใช้ form เดียวกันกับการเพิ่ม
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("teamMember") TeamMember updatedMember,
                         BindingResult result,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("teams", ambulanceTeamRepository.findAll());
            return "team-member/form";
        }

        TeamMember existing = teamMemberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ไม่พบสมาชิก ID: " + id));

        existing.setName(updatedMember.getName());
        existing.setRole(updatedMember.getRole());
        existing.setTeam(updatedMember.getTeam());

        teamMemberRepository.save(existing);
        return "redirect:/team-members";
    }

    @PostMapping("/{id}/delete")
    public String deleteTeamMember(@PathVariable Long id) {
        teamMemberRepository.deleteById(id);
        return "redirect:/team-members";
    }

}
