package com.wewe.bottrading.database;

import okhttp3.*;

public class BinanceMarketData {
    private static final String API_URL = "https://api.binance.com/api/v3/ticker/price?symbol=BTCUSDT";

    public static void main(String[] args) throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(API_URL).build();
        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            System.out.println("Market Price: " + response.body().string());
        } else {
            System.out.println("Error: " + response.code());
        }
    }
}

