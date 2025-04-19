package com.wewe.weweShop.service;

import com.wewe.weweShop.model.Product;
import com.wewe.weweShop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    // ✅ แสดงสินค้าทั้งหมด
    public List<Product> findAll() {
        return productRepository.findAll();
    }
    // ✅ แสดงสินค้าทั้งหมด
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // ✅ แสดงสินค้ารายตัว
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
    }

    // ✅ เพิ่มสินค้าใหม่
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    // ✅ แก้ไขสินค้า
    public Product updateProduct(Long id, Product updatedProduct) {
        Product product = getProductById(id);
        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setPrice(updatedProduct.getPrice());
        product.setImageUrl(updatedProduct.getImageUrl());
        return productRepository.save(product);
    }

    // ✅ ลบสินค้า
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
