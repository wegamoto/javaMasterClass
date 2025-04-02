package com.wewe.bottrading.model;

import okhttp3.*;
import java.util.*;

public class TradeExecution {

    private static final String API_URL = "https://api.binance.com/api/v3/order";
    private static final String API_KEY = "your_api_key";

    public void placeTrade(String side, double quantity) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("symbol", "BTCUSDT");
        params.put("side", side); // "BUY" or "SELL"
        params.put("type", "MARKET");
        params.put("quantity", String.valueOf(quantity));

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(null, new byte[0]);
        String url = buildUrlWithParams(API_URL, params);

        Request request = new Request.Builder()
                .url(url)
                .header("X-MBX-APIKEY", API_KEY)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("Trade execution failed: " + response.body().string());
            }
            System.out.println("Trade executed: " + response.body().string());
        }
    }

    private String buildUrlWithParams(String baseUrl, Map<String, String> params) {
        StringBuilder urlBuilder = new StringBuilder(baseUrl);
        urlBuilder.append("?");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return urlBuilder.toString();
    }
}

