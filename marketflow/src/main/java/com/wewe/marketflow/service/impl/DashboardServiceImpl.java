package com.wewe.marketflow.service.impl;

import com.wewe.marketflow.dto.DashboardSummaryDTO;
import com.wewe.marketflow.model.BudgetItem;
import com.wewe.marketflow.model.TaskStatus;
import com.wewe.marketflow.repository.BudgetItemRepository;
import com.wewe.marketflow.repository.CampaignRepository;
import com.wewe.marketflow.repository.MarketingTaskRepository;
import com.wewe.marketflow.service.DashboardService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final CampaignRepository campaignRepo;
    private final MarketingTaskRepository taskRepo;
    private final BudgetItemRepository budgetRepo;

    public DashboardServiceImpl(CampaignRepository campaignRepo,
                                MarketingTaskRepository taskRepo,
                                BudgetItemRepository budgetRepo) {
        this.campaignRepo = campaignRepo;
        this.taskRepo = taskRepo;
        this.budgetRepo = budgetRepo;
    }

    @Override
    public DashboardSummaryDTO getSummary() {
        long totalCampaigns = campaignRepo.count();
        long totalTasks = taskRepo.count();
        long totalBudgetItems = budgetRepo.count();
        BigDecimal totalSpent = budgetRepo.totalSpent();

        Map<String, Long> statusCount = new HashMap<>();
        for (TaskStatus status : TaskStatus.values()) {
            long count = taskRepo.countByStatus(status);
            statusCount.put(status.name(), count);
        }

        return new DashboardSummaryDTO(
                totalCampaigns,
                totalTasks,
                totalBudgetItems,
                totalSpent,
                statusCount
        );
    }

    @Override
    public Map<String, BigDecimal> getSpendingByCategory() {
        List<BudgetItem> items = budgetRepo.findAll();
        return items.stream()
                .collect(Collectors.groupingBy(
                        BudgetItem::getCategory,
                        Collectors.mapping(BudgetItem::getAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));
    }

}
