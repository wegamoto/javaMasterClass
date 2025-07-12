package com.wewe.marketflow.controller;

import com.wewe.marketflow.model.Campaign;
import com.wewe.marketflow.service.CampaignService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping
    public List<Campaign> getAll() {
        return campaignService.getAll();
    }

    @PostMapping
    public Campaign create(@RequestBody Campaign campaign) {
        return campaignService.save(campaign);
    }

    @GetMapping("/{id}")
    public Campaign getById(@PathVariable Long id) {
        return campaignService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        campaignService.delete(id);
    }
}

