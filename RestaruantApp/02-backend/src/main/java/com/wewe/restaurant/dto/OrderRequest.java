package com.wewe.restaurant.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OrderRequest {
    // ✅ Getter และ Setter
    private Long userId;
    private String status;
    private List<OrderItemRequest> menuItems; // ต้องแน่ใจว่า field นี้ตรงกับ JSON

    // ✅ Constructor ว่าง (จำเป็นสำหรับ Jackson ในการ Deserialize JSON)
    public OrderRequest() {
    }

    // ✅ Constructor พร้อมพารามิเตอร์ (ใช้ใน Unit Test หรือสร้าง Object)
    public OrderRequest(Long userId, String status, List<OrderItemRequest> menuItems) {
        this.userId = userId;
        this.status = status;
        this.menuItems = menuItems;
    }
}




