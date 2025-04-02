package com.wewe.bottrading.model;

public class RiskManagement {

    private static double stopLossThreshold = 0.05; // 5% Stop Loss

    public boolean shouldStopLoss(double entryPrice, double currentPrice) {
        double lossPercentage = (entryPrice - currentPrice) / entryPrice;
        return lossPercentage >= stopLossThreshold;
    }

    public boolean shouldTakeProfit(double entryPrice, double currentPrice) {
        double profitPercentage = (currentPrice - entryPrice) / entryPrice;
        return profitPercentage >= 0.10; // 10% profit
    }
}

