package com.wewe.bottrading.service;

import com.wewe.bottrading.model.TradingStrategy;
import org.springframework.stereotype.Service;

@Service
public class TradingStrategyService {

    private final TradingStrategy strategy;

    public TradingStrategyService() {
        this.strategy = new TradingStrategy();
    }

    public double predictPriceChange(double currentPrice) throws Exception {
        // ทำการทำนายการเปลี่ยนแปลงราคาจากกลยุทธ์
        return strategy.predictPriceChange(currentPrice);
    }
}

