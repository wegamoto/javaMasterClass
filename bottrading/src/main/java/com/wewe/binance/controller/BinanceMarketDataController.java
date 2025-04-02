package com.wewe.binance.controller;

import com.wewe.binance.service.BinanceMarketDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BinanceMarketDataController {

    @Autowired
    private BinanceMarketDataService binanceMarketDataService;

    @GetMapping("/market-data")
    public String getMarketData(@RequestParam String symbol) {
        return binanceMarketDataService.getMarketData(symbol);
    }
}

