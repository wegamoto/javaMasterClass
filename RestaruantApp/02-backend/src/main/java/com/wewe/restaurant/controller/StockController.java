package com.wewe.restaurant.controller;

import com.wewe.restaurant.model.Stock;
import com.wewe.restaurant.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @PostMapping
    public Stock addStock(@RequestBody Stock stock) {
        return stockService.addStock(stock);
    }

    @PutMapping("/{id}")
    public Stock updateStock(@PathVariable Long id, @RequestBody Stock updatedStock) {
        return stockService.updateStock(id, updatedStock);
    }

    @GetMapping("/{id}")
    public Optional<Stock> getStock(@PathVariable Long id) {
        return stockService.getStock(id);
    }

    @DeleteMapping("/{id}")
    public void deleteStock(@PathVariable Long id) {
        stockService.deleteStock(id);
    }
}
