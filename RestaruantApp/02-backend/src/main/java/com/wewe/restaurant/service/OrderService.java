package com.wewe.restaurant.service;

import com.wewe.restaurant.model.*;
import com.wewe.restaurant.repository.MenuItemRepository;
import com.wewe.restaurant.repository.OrderRepository;
import com.wewe.restaurant.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private MenuItemRepository menuItemRepository;

    // เพิ่ม MenuItemRepository ใน constructor
    public OrderService(OrderRepository orderRepository, UserRepository userRepository,
                        MenuItemRepository menuItemRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.menuItemRepository = menuItemRepository;
    }

    // ✅ สร้าง Order ใหม่
    public Order createOrder(Long userId, List<Long> menuItemIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<MenuItem> menuItems = menuItemRepository.findAllById(menuItemIds);
        if (menuItems.isEmpty()) {
            throw new RuntimeException("Menu items not found");
        }

        Order order = new Order();
        order.setUser(user);
        order.setMenuItems(menuItems);
        order.setStatus(String.valueOf(OrderStatus.PENDING));

        return orderRepository.save(order);
    }

    // ✅ ดึงออเดอร์ทั้งหมด
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // ✅ ดึงออเดอร์ตาม ID
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // ✅ อัปเดต Order
    public Order updateOrder(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(String.valueOf(OrderStatus.PENDING));
        return orderRepository.save(order);
    }

    // ✅ ลบออเดอร์
    public boolean cancelOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found");
        }
        orderRepository.deleteById(id);
        return false;
    }
}


