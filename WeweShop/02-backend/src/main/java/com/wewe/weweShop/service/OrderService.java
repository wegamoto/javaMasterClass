package com.wewe.weweShop.service;

import com.wewe.weweShop.model.CartItem;
import com.wewe.weweShop.model.Order;
import com.wewe.weweShop.model.OrderItem;
import com.wewe.weweShop.model.User;
import com.wewe.weweShop.repository.OrderRepository;
import com.wewe.weweShop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartService cartService;

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

    public boolean hasNewOrders() {
        return orderRepository.countByStatus("NEW") > 0;
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

    public Order createOrderFromCart(String userEmail) {
        // ดึงข้อมูลรายการสินค้าจากตะกร้า
        List<CartItem> cartItems = cartService.getCartItems(userEmail);
        if (cartItems == null || cartItems.isEmpty()) return null;

        // สร้างคำสั่งซื้อใหม่
        Order order = new Order();
        order.setCustomerEmail(userEmail);
        order.setOrderDate(LocalDateTime.now());

        // แปลง CartItem เป็น OrderItem
        List<OrderItem> items = cartItems.stream().map(item -> {
            OrderItem oi = new OrderItem();
            oi.setProductId(item.getProduct().getId());
            oi.setProductName(item.getProductName());
            oi.setPrice(item.getPrice());
            oi.setQuantity(item.getQuantity());
            oi.setOrder(order);
            return oi;
        }).collect(Collectors.toList());

//        double total = items.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
        // คำนวณยอดรวม (totalAmount) โดยใช้ BigDecimal
        BigDecimal total = items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // ตั้งค่าราคาสินค้ารวมในคำสั่งซื้อ
        order.setTotalAmount(total);
        order.setItems(items);

        // เคลียร์ตะกร้าหลังการสั่งซื้อ
        cartService.clearCart(userEmail); // clear cart after checkout

        // บันทึกคำสั่งซื้อใหม่
        return orderRepository.save(order);
    }
}
