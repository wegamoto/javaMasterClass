package com.wewe.marketflow.service;

import com.wewe.marketflow.model.BudgetItem;

import java.util.List;

public interface BudgetItemService {
    List<BudgetItem> getAll();

    BudgetItem getById(Long id);

    List<BudgetItem> findByCampaignId(Long campaignId);

    BudgetItem save(BudgetItem item);

    void delete(Long id);
}
