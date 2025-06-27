package com.wewe.temjaiShop.controller;

import com.wewe.temjaiShop.model.Product;
import com.wewe.temjaiShop.repository.ProductRepository;
import com.wewe.temjaiShop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    @Autowired
    public ProductRestController(ProductRepository productRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll(); // ส่งกลับข้อมูล JSON ของสินค้า
    }

    @PostMapping("/{id}/increase-stock")
    public ResponseEntity<String> increaseStock(@PathVariable Long id, @RequestParam int quantity) {
        productService.increaseStock(id, quantity);
        return ResponseEntity.ok("Stock increased successfully");
    }

    @PostMapping("/{id}/decrease-stock")
    public ResponseEntity<String> decreaseStock(@PathVariable Long id, @RequestParam int quantity, String purchase) {
        productService.decreaseStock(id, quantity);
        return ResponseEntity.ok("Stock decreased successfully");
    }
}
