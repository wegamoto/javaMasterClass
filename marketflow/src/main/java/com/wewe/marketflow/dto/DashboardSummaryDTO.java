package com.wewe.marketflow.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class DashboardSummaryDTO {

    private long totalCampaigns;
    private long totalTasks;
    private long totalBudgetItems;
    private BigDecimal totalBudgetSpent;
    private Map<String, Long> taskStatusCount;

    public DashboardSummaryDTO(long totalCampaigns, long totalTasks,
                               long totalBudgetItems, BigDecimal totalBudgetSpent,
                               Map<String, Long> taskStatusCount) {
        this.totalCampaigns = totalCampaigns;
        this.totalTasks = totalTasks;
        this.totalBudgetItems = totalBudgetItems;
        this.totalBudgetSpent = totalBudgetSpent != null ? totalBudgetSpent : BigDecimal.ZERO;
        this.taskStatusCount = taskStatusCount;
    }

    // Add Getters (and Setters if needed)
    public long getTotalCampaigns() {
        return totalCampaigns;
    }

    public long getTotalTasks() {
        return totalTasks;
    }

    public long getTotalBudgetItems() {
        return totalBudgetItems;
    }

    public BigDecimal getTotalBudgetSpent() {
        return totalBudgetSpent;
    }

    public Map<String, Long> getTaskStatusCount() {
        return taskStatusCount;
    }
}
