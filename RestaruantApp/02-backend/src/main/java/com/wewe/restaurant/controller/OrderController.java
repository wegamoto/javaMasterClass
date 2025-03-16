package com.wewe.restaurant.controller;

import com.wewe.restaurant.model.Order;
import com.wewe.restaurant.model.OrderRequest;
import com.wewe.restaurant.service.OrderService;
import com.wewe.restaurant.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
    }

    // ✅ สร้างออเดอร์ใหม่
    @PostMapping("/orders/create")
    public ResponseEntity<Order> createOrder(@RequestParam Long userId, @RequestBody List<Long> menuItemIds) {
        Order createOrder = orderService.createOrder(userId, menuItemIds);
        return ResponseEntity.ok(createOrder);
    }

    // ✅ ดึงรายการออเดอร์ทั้งหมด
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // ✅ ดึงข้อมูลออเดอร์ตาม ID
    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    // ✅ Update Order status
    @PutMapping("/orders/{id}/update")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestParam String status) {
        Order updatedOrder = orderService.updateOrder(id, status);
        if (updatedOrder != null) {
            return ResponseEntity.ok(updatedOrder);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // ✅ ลบออเดอร์  Cancel Order
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long id) {
        boolean isDeleted = orderService.cancelOrder(id);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}


