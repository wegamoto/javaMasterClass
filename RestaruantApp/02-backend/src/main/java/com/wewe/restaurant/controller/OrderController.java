package com.wewe.restaurant.controller;

import com.wewe.restaurant.model.Order;
import com.wewe.restaurant.model.OrderRequest;
import com.wewe.restaurant.model.User;
import com.wewe.restaurant.service.OrderService;
import com.wewe.restaurant.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    // ✅ สร้างออเดอร์ใหม่
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        User user = userService.findById(order.getUser().getId());
        order.setUser(user);

        Order savedOrder = orderService.createOrder(order);
        return ResponseEntity.ok(savedOrder);
    }


    // ✅ ดึงรายการออเดอร์ทั้งหมด
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // ✅ ดึงข้อมูลออเดอร์ตาม ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    // ✅ อัปเดตข้อมูลออเดอร์
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) {
        return ResponseEntity.ok(orderService.updateOrder(id, order));
    }

    // ✅ ลบออเดอร์
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully.");
    }
}


