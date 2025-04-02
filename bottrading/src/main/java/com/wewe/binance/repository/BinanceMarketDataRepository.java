package com.wewe.binance.repository;

import com.wewe.binance.model.MarketData;

public interface BinanceMarketDataRepository extends JpaRepository<MarketData, Long> {
    // Define custom queries here if needed
}
