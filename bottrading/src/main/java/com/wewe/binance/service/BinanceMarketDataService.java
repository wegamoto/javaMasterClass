package com.wewe.binance.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BinanceMarketDataService {

    private final String API_URL = "https://api.binance.com/api/v3/ticker/price?symbol=";

    public String getMarketData(String symbol) {
        RestTemplate restTemplate = new RestTemplate();
        String url = API_URL + symbol.toUpperCase();
        return restTemplate.getForObject(url, String.class);
    }
}
