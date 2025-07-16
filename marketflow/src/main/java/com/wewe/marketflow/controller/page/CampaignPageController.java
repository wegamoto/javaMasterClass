package com.wewe.marketflow.controller.page;

import com.wewe.marketflow.model.Campaign;
import com.wewe.marketflow.repository.CampaignRepository;
import com.wewe.marketflow.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/campaigns")
@RequiredArgsConstructor
public class CampaignPageController {

    private final CampaignRepository campaignRepo;
    private final UserRepository userRepo;

    @GetMapping
    public String listCampaigns(Model model) {
        List<Campaign> campaigns = campaignRepo.findAll();
        model.addAttribute("campaigns", campaigns);
        return "campaign/list"; // templates/campaign/list.html
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("campaign", new Campaign());
        model.addAttribute("users", userRepo.findAll());
        return "campaign/form"; // templates/campaign/form.html
    }

    @PostMapping("/create")
    public String createCampaign(@Valid @ModelAttribute Campaign campaign, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users", userRepo.findAll());
            return "campaign/form";
        }
        campaignRepo.save(campaign);
        return "redirect:/campaigns";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Campaign campaign = campaignRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid campaign ID: " + id));
        model.addAttribute("campaign", campaign);
        model.addAttribute("users", userRepo.findAll());
        return "campaign/form";
    }

    @PostMapping("/edit/{id}")
    public String updateCampaign(@PathVariable Long id, @Valid @ModelAttribute Campaign campaign, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users", userRepo.findAll());
            return "campaign/form";
        }
        campaign.setId(id);
        campaignRepo.save(campaign);
        return "redirect:/campaigns";
    }

    @PostMapping("/save")
    public String saveCampaign(
            @Valid @ModelAttribute("campaign") Campaign campaign,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("users", userRepo.findAll());
            return "campaign/form";
        }

        // ถ้าเป็นการแก้ไข ตรวจสอบว่ามี campaign เดิมใน DB หรือไม่
        if (campaign.getId() != null) {
            campaignRepo.findById(campaign.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid campaign ID: " + campaign.getId()));
        }

        campaignRepo.save(campaign);
        return "redirect:/campaigns";
    }

    @GetMapping("/delete/{id}")
    public String deleteCampaign(@PathVariable Long id) {
        campaignRepo.deleteById(id);
        return "redirect:/campaigns";
    }
}

