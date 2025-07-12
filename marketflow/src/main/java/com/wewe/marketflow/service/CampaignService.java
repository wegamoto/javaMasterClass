package com.wewe.marketflow.service;

import com.wewe.marketflow.model.Campaign;

import java.util.List;

public interface CampaignService {
    List<Campaign> getAll();
    Campaign save(Campaign campaign);
    Campaign getById(Long id);
    void delete(Long id);
}

