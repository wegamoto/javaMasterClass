package com.wewe.restaurant.model;

import java.util.List;

public class OrderRequest {
    private Long userId;
    private List<MenuItemRequest> menuItems;
    private String status;

    // Getter and Setter methods
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<MenuItemRequest> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItemRequest> menuItems) {
        this.menuItems = menuItems;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

