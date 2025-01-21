package dev.lpa;

import static dev.lpa.BitcoinPriceFetcher.fetchBitcoinPrice;

public class Main {

    public static void main(String[] args) {
        System.out.println("Fetching Bitcoin price...");
        fetchBitcoinPrice();
    }
}
