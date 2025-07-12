package com.wewe.marketflow.service.impl;

import com.wewe.marketflow.model.BudgetItem;
import com.wewe.marketflow.repository.BudgetItemRepository;
import com.wewe.marketflow.service.BudgetItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetItemServiceImpl implements BudgetItemService {

    private final BudgetItemRepository budgetItemRepository;

    public BudgetItemServiceImpl(BudgetItemRepository budgetItemRepository) {
        this.budgetItemRepository = budgetItemRepository;
    }

    @Override
    public List<BudgetItem> getAll() {
        return budgetItemRepository.findAll();
    }

    @Override
    public BudgetItem getById(Long id) {
        return budgetItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BudgetItem not found with ID: " + id));
    }

    @Override
    public List<BudgetItem> findByCampaignId(Long campaignId) {
        return budgetItemRepository.findByCampaignId(campaignId);
    }

    @Override
    public BudgetItem save(BudgetItem item) {
        return budgetItemRepository.save(item);
    }

    @Override
    public void delete(Long id) {
        budgetItemRepository.deleteById(id);
    }
}
