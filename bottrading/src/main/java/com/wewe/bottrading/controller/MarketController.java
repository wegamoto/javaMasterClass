package com.wewe.bottrading.controller;

import com.wewe.bottrading.service.MarketDataService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MarketController {

    private final MarketDataService marketDataService;

    public MarketController(MarketDataService marketDataService) {
        this.marketDataService = marketDataService;
    }

    @GetMapping("/market/price")
    public double getMarketPrice() {
        return marketDataService.getMarketPrice();
    }
}

