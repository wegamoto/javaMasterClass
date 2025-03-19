package com.wewe.restaurant.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderItemRequest {
    private Long menuItemId;
    private int quantity;

    // ✅ Constructor
    public OrderItemRequest() {}

    public OrderItemRequest(Long menuItemId, int quantity) {
        this.menuItemId = menuItemId;
        this.quantity = quantity;
    }
}

