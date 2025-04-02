package com.wewe.bottrading.controller;

import com.wewe.bottrading.service.TradeExecutionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TradingController {

    private final TradeExecutionService tradeExecutionService;

    public TradingController(TradeExecutionService tradeExecutionService) {
        this.tradeExecutionService = tradeExecutionService;
    }

    @PostMapping("/trade/execute")
    public String placeTrade(@RequestParam String side, @RequestParam double quantity) {
        try {
            tradeExecutionService.placeTrade(side, quantity);
            return "Trade executed successfully";
        } catch (Exception e) {
            return "Trade execution failed: " + e.getMessage();
        }
    }
}

