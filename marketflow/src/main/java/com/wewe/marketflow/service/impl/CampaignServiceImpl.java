package com.wewe.marketflow.service.impl;

import com.wewe.marketflow.model.Campaign;
import com.wewe.marketflow.repository.CampaignRepository;
import com.wewe.marketflow.service.CampaignService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampaignServiceImpl implements CampaignService {

    private final CampaignRepository campaignRepository;

    public CampaignServiceImpl(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    @Override
    public List<Campaign> getAll() {
        return campaignRepository.findAll();
    }

    @Override
    public Campaign getById(Long id) {
        return campaignRepository.findById(id).orElseThrow();
    }

    @Override
    public Campaign save(Campaign campaign) {
        return campaignRepository.save(campaign);
    }

    @Override
    public void delete(Long id) {
        campaignRepository.deleteById(id);
    }
}

