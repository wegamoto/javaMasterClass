package com.restaurant.service;

import com.restaurant.model.Order;
import com.restaurant.model.OrderItem;
import com.restaurant.model.OrderStatus;
import com.restaurant.repository.OrderItemRepository;
import com.restaurant.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    // ดึงข้อมูลคำสั่งซื้อทั้งหมด
    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

    // ดึงข้อมูลคำสั่งซื้อจาก ID
    public Order getOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.orElse(null); // ถ้าไม่พบ จะคืนค่า null
    }

    // สร้างคำสั่งซื้อใหม่
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order createOrder(Long userId, List<OrderItemRequest> orderItems) {
        Order order = new Order();
        order.setUser(new User(userId));
        order.setStatus(OrderStatus.PENDING);

        double totalPrice = 0.0;
        List<OrderItem> items = new ArrayList<>();

        for (OrderItemRequest itemReq : orderItems) {
            MenuItem menuItem = menuItemRepository.findById(itemReq.getMenuItemId())
                    .orElseThrow(() -> new RuntimeException("Menu item not found"));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setMenuItem(menuItem);
            item.setQuantity(itemReq.getQuantity());
            item.setPrice(menuItem.getPrice() * itemReq.getQuantity());

            items.add(item);
            totalPrice += item.getPrice();
        }

        order.setTotalPrice(totalPrice);
        order.setItems(items);
        return orderRepository.save(order);
    }

    // อัพเดตคำสั่งซื้อ
    public Order updateOrder(Long id, Order orderDetails) {
        Optional<Order> existingOrder = orderRepository.findById(id);
        if (existingOrder.isPresent()) {
            Order order = existingOrder.get();
            order.setCustomerName(orderDetails.getCustomerName());
            order.setOrderDate(orderDetails.getOrderDate());
            order.setItems(orderDetails.getItems());
            return orderRepository.save(order);
        }
        return null;
    }

    // ลบคำสั่งซื้อ
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }
}

