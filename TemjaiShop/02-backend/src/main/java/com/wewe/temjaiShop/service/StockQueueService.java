package com.wewe.temjaiShop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class StockQueueService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String STOCK_QUEUE = "stock:increase:queue";

    public void enqueueStockUpdate(Long productId, int quantity) {
        String message = productId + "," + quantity;
        redisTemplate.opsForList().rightPush(STOCK_QUEUE, message);
    }
}

