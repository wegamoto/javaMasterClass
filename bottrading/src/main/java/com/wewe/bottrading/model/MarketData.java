package com.wewe.bottrading.model;

import okhttp3.*;
import java.io.IOException;

public class MarketData {

    private static final String API_URL = "https://api.binance.com/api/v3/ticker/price?symbol=BTCUSDT";

    public double getMarketPrice() throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(API_URL).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String jsonResponse = response.body().string();
                return parsePriceFromJson(jsonResponse);
            } else {
                throw new Exception("Failed to fetch market data");
            }
        } catch (IOException e) {
            throw new Exception("Network error: " + e.getMessage(), e);
        }
    }

    private double parsePriceFromJson(String json) {
        return Double.parseDouble(json.substring(json.indexOf("price") + 7, json.indexOf("}") - 1));
    }
}

