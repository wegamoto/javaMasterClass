package com.wewe.weweShop.service;

import com.wewe.weweShop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class StockWorker {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Scheduled(fixedDelay = 1000)
    public void processStockQueue() {
        String item;
        while ((item = redisTemplate.opsForList().leftPop("stock:increase:queue")) != null) {
            String[] parts = item.split(",");
            Long productId = Long.parseLong(parts[0]);
            int quantity = Integer.parseInt(parts[1]);

            productRepository.increaseStockNative(productId, quantity);
        }
    }
}

