package com.wewe.weweShop.service;

import com.wewe.weweShop.model.Product;
import com.wewe.weweShop.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public void save(Product product) {
        productRepository.save(product); // บันทึกหรืออัปเดต
    }

    public boolean hasLowStock() {
        return productRepository.existsByStockLessThan(5);
    }

    public List<Product> getLowStockProducts() {
        return productRepository.findAll().stream()
                .filter(product -> product.getStock() < 5)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
