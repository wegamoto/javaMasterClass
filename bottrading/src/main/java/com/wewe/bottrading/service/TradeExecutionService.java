package com.wewe.bottrading.service;


import com.wewe.bottrading.model.TradeExecution;
import org.springframework.stereotype.Service;

@Service
public class TradeExecutionService {

    private final TradeExecution tradeExecution;

    public TradeExecutionService() {
        this.tradeExecution = new TradeExecution();
    }

    public void placeTrade(String side, double quantity) throws Exception {
        tradeExecution.placeTrade(side, quantity);
    }
}

