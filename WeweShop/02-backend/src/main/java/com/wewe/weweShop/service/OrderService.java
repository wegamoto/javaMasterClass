package com.wewe.weweShop.service;

import com.wewe.weweShop.model.Order;
import com.wewe.weweShop.model.User;
import com.wewe.weweShop.repository.OrderRepository;
import com.wewe.weweShop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    private UserRepository userRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    // Inject Repository ผ่าน Constructor
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public boolean hasNewOrders() {
        // สมมติว่าเรามีสถานะ "NEW" หรือ "PENDING" สำหรับคำสั่งซื้อใหม่
        return orderRepository.countByStatus("NEW") > 0;  // ตรวจสอบว่า "NEW" มีคำสั่งซื้อที่ยังไม่ได้รับการดำเนินการ
    }

    public List<Order> getOrdersByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return orderRepository.findByUser(user);
    }

    public Order getOrderByIdAndEmail(Long id, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return orderRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new NoSuchElementException("Order not found or access denied"));
    }

}

