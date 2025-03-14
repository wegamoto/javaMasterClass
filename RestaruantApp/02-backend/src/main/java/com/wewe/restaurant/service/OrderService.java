package com.wewe.restaurant.service;

import com.wewe.restaurant.model.MenuItem;
import com.wewe.restaurant.model.Order;
import com.wewe.restaurant.model.OrderStatus;
import com.wewe.restaurant.model.User;
import com.wewe.restaurant.repository.MenuItemRepository;
import com.wewe.restaurant.repository.OrderRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private MenuItemRepository menuItemRepository;

    // เพิ่ม MenuItemRepository ใน constructor
    public OrderService(OrderRepository orderRepository, MenuItemRepository menuItemRepository) {
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
    }

    // ✅ สร้างออเดอร์ใหม่
    public Order createOrder(Order order) {
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

    // ✅ อัปเดตออเดอร์
    public Order updateOrder(Long id, Order updatedOrder) {
        return orderRepository.findById(id).map(order -> {
            order.setCustomerName(updatedOrder.getCustomerName());
            order.setOrderDate(updatedOrder.getOrderDate());
            order.setStatus(updatedOrder.getStatus());
            return orderRepository.save(order);
        }).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // ✅ ลบออเดอร์
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found");
        }
        orderRepository.deleteById(id);
    }

    public Order createOrder(User user, List<MenuItem> menuItems, String status) {
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        // ใช้ menuItemRepository เพื่อดึงข้อมูล MenuItem จากฐานข้อมูล
        List<MenuItem> items = menuItems.stream()
                .map(itemRequest -> menuItemRepository.findById(itemRequest.getId())
                        .orElseThrow(() -> new RuntimeException("Menu item not found: " + itemRequest.getId())))
                .collect(Collectors.toList());

        order.setMenuItems(items);
        return orderRepository.save(order);
    }

}


