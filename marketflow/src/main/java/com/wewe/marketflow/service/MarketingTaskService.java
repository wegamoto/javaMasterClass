package com.wewe.marketflow.service;

import com.wewe.marketflow.model.MarketingTask;

import java.util.List;

public interface MarketingTaskService {

    List<MarketingTask> getAll();

    List<MarketingTask> findByCampaignId(Long campaignId);

    MarketingTask save(MarketingTask task);

    MarketingTask getById(Long id);

    void delete(Long id);
}
