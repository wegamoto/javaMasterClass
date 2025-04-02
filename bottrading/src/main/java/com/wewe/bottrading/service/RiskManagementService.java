package com.wewe.bottrading.service;

import com.wewe.bottrading.model.RiskManagement;
import org.springframework.stereotype.Service;

@Service
public class RiskManagementService {

    private final RiskManagement riskManagement;

    public RiskManagementService() {
        this.riskManagement = new RiskManagement();
    }

    public boolean shouldStopLoss(double entryPrice, double currentPrice) {
        return riskManagement.shouldStopLoss(entryPrice, currentPrice);
    }

    public boolean shouldTakeProfit(double entryPrice, double currentPrice) {
        return riskManagement.shouldTakeProfit(entryPrice, currentPrice);
    }
}

