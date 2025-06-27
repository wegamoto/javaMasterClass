package com.wewe.weweShop.model;

public class ProductDto {

    private String name;
    private String category;
    private String formattedPrice;
    private int stock;

    // Constructor
    public ProductDto() {}

    public ProductDto(String name, String category, String formattedPrice, int stock) {
        this.name = name;
        this.category = category;
        this.formattedPrice = formattedPrice;
        this.stock = stock;
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFormattedPrice() {
        return formattedPrice;
    }

    public void setFormattedPrice(String formattedPrice) {
        this.formattedPrice = formattedPrice;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    // Optional: toString method if needed
    @Override
    public String toString() {
        return "ProductDto{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", formattedPrice='" + formattedPrice + '\'' +
                ", stock=" + stock +
                '}';
    }
}
