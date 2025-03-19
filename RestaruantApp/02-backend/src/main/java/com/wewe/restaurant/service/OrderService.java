package com.wewe.restaurant.service;

import com.wewe.restaurant.dto.OrderRequest;
import com.wewe.restaurant.model.*;
import com.wewe.restaurant.repository.MenuItemRepository;
import com.wewe.restaurant.repository.OrderRepository;
import com.wewe.restaurant.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;

    // ✅ ใช้ Constructor Injection อย่างถูกต้อง
    public OrderService(OrderRepository orderRepository, UserRepository userRepository,
                        MenuItemRepository menuItemRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.menuItemRepository = menuItemRepository;
    }

    // ✅ สร้าง Order ใหม่
    public Order createOrder(OrderRequest orderRequest) {
        if(orderRequest == null || orderRequest.getUserId() == null || orderRequest.getMenuItems() == null) {
            throw new IllegalArgumentException("Invalid order request: userId or menuItems is missing");
        }

        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.valueOf(orderRequest.getStatus()));
        order.setOrderDate(LocalDateTime.now());

        // บันทึก Order ก่อนเพื่อให้มี ID
        order = orderRepository.save(order);

        Order finalOrder = order;

        // เปลี่ยนจาก List เป็น Set
        Set<OrderItem> orderItems = orderRequest.getMenuItems().stream().map(item -> {
            if(item.getMenuItemId() == null) {
                throw new RuntimeException("MenuItem ID is missing in request");
            }

            MenuItem menuItem = menuItemRepository.findById(item.getMenuItemId())
                    .orElseThrow(() -> new RuntimeException("Menu item not found: " + item.getMenuItemId()));

            OrderItem orderItem = new OrderItem();
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(menuItem.getPrice());  // เก็บราคาเมนูในขณะนั้น
            orderItem.setOrder(finalOrder);

            return orderItem;
        }).collect(Collectors.toSet());  // เปลี่ยนจาก List เป็น Set

        order.setOrderItems(orderItems); // กำหนดค่า OrderItems ก่อนบันทึก
        order.calculateTotalPrice(); // คำนวณก่อนบันทึก

        return orderRepository.save(order);
    }
    // ดึงออเดอร์ทั้งหมด
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // ✅ ดึงออเดอร์ตาม ID
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // ✅ อัปเดตสถานะ Order
    public Order updateOrder(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // ตรวจสอบว่าสถานะที่ส่งมาเป็นค่าที่ถูกต้องของ ENUM OrderStatus
        try {
            order.setStatus(OrderStatus.valueOf(OrderStatus.valueOf(status).toString()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid order status");
        }

        return orderRepository.save(order);
    }

    // ✅ ลบออเดอร์
    public boolean cancelOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found");
        }
        orderRepository.deleteById(id);
        return true; // เปลี่ยนจาก false เป็น true
    }
}


