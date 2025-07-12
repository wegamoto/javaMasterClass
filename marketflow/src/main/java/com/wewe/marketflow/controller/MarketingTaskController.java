package com.wewe.marketflow.controller;

import com.wewe.marketflow.model.MarketingTask;
import com.wewe.marketflow.service.MarketingTaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class MarketingTaskController {

    private final MarketingTaskService marketingTaskService;

    public MarketingTaskController(MarketingTaskService marketingTaskService) {
        this.marketingTaskService = marketingTaskService;
    }

    @GetMapping
    public List<MarketingTask> getAll() {
        return marketingTaskService.getAll();
    }

    @PostMapping
    public MarketingTask create(@RequestBody MarketingTask task) {
        return marketingTaskService.save(task);
    }

    @GetMapping("/campaign/{campaignId}")
    public List<MarketingTask> getByCampaign(@PathVariable Long campaignId) {
        return marketingTaskService.findByCampaignId(campaignId);
    }
}

