package com.wewe.restaurant.service;

import com.wewe.restaurant.model.Stock;
import com.wewe.restaurant.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    // เพิ่ม Stock ใหม่
    public Stock addStock(Stock stock) {
        return stockRepository.save(stock);
    }

    // อัปเดต Stock
    public Stock updateStock(Long id, Stock updatedStock) {
        Stock existingStock = stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        existingStock.setName(updatedStock.getName());
        existingStock.setQuantity(updatedStock.getQuantity());
        existingStock.setPrice(updatedStock.getPrice());
        existingStock.setMenuItem(updatedStock.getMenuItem());

        return stockRepository.save(existingStock);
    }

    // ค้นหาสต๊อกตาม ID
    public Optional<Stock> getStock(Long id) {
        return stockRepository.findById(id);
    }

    // ลบ Stock
    public void deleteStock(Long id) {
        stockRepository.deleteById(id);
    }
}
