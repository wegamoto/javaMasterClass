package com.wewe.weweShop.service;

import com.wewe.weweShop.model.Product;
import com.wewe.weweShop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    @Autowired
    private ProductRepository productRepository;

//    // ตรวจสอบ stock
//    public boolean isInStock(Long productId, int requestedQuantity) {
//        Product product = productRepository.findById(productId).orElseThrow();
//        return product.getStock() >= requestedQuantity;
//    }
//
//    // หักสต๊อกเมื่อสั่งซื้อ
//    public void decreaseStock(Long productId, int quantity, String purchase) {
//        Product product = productRepository.findById(productId).orElseThrow();
//        if (product.getStock() < quantity) {
//            throw new RuntimeException("Not enough stock");
//        }
//        product.setStock(product.getStock() - quantity);
//        productRepository.save(product);
//    }
//
//    // เพิ่ม stock
//    public void increaseStock(Long productId, int quantity) {
//        Product product = productRepository.findById(productId).orElseThrow();
//        product.setStock(product.getStock() + quantity);
//        productRepository.save(product);
//    }

}

