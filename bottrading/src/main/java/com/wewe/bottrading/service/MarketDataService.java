package com.wewe.bottrading.service;

import com.wewe.bottrading.model.MarketData;
import org.springframework.stereotype.Service;

@Service
public class MarketDataService {

    private final MarketData marketData;

    public MarketDataService() {
        this.marketData = new MarketData();
    }

    public double getMarketPrice() {
        try {
            return marketData.getMarketPrice();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching market price: " + e.getMessage());
        }
    }
}

