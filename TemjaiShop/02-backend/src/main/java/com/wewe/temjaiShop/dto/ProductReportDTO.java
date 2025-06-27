package com.wewe.temjaiShop.dto;

import com.wewe.temjaiShop.model.Product;

import java.math.BigDecimal;

public class ProductReportDTO {
    private final String name;
    private final BigDecimal price;
    private final int stockQuantity;
    private final String categoryName;

    public ProductReportDTO(Product product) {
        this.name = product.getName();
        this.price = product.getPrice();
        this.stockQuantity = product.getStockQuantity();
        this.categoryName = product.getCategory() != null ? product.getCategory().getName() : "ไม่ระบุ";
    }

    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public int getStockQuantity() { return stockQuantity; }
    public String getCategoryName() { return categoryName; }

    public BigDecimal getTotalValue() {
        return price.multiply(BigDecimal.valueOf((long) stockQuantity));
    }
}


