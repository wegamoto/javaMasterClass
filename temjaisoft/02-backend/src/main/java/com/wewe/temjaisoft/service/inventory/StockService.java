package com.wewe.temjaisoft.service.inventory;

import com.wewe.temjaisoft.model.inventory.Stock;
import com.wewe.temjaisoft.repository.inventory.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<Stock> findAll() {
        return stockRepository.findAll();
    }

    public Optional<Stock> findById(Long id) {
        return stockRepository.findById(id);
    }

    public List<Stock> findByProductId(Long productId) {
        return stockRepository.findByProductId(productId);
    }

    public void save(Stock stock) {
        stockRepository.save(stock);
    }

    public void deleteById(Long id) {
        stockRepository.deleteById(id);
    }
}

