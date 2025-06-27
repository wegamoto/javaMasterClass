package com.wewe.weweShop.service;

import com.wewe.weweShop.model.*;
import com.wewe.weweShop.repository.OrderRepository;
import com.wewe.weweShop.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, CartService cartService, ProductService productService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartService = cartService;
        this.productService = productService;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByUser(String userEmail) {
        return null;
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

    @Transactional
    public Order createOrderFromCart(Principal principal) {

        String userEmail = principal.getName();
//        log.info("Checkout by userEmail: {}", userEmail);

        // ดึงข้อมูลรายการสินค้าจากตะกร้า ไม่ได้ระบุ userEmail
        List<CartItem> cartItems = cartService.getCartItems(userEmail);

        // ✅ ตรวจสอบและโยนข้อผิดพลาดหากตะกร้าว่าง
        if (cartItems == null || cartItems.isEmpty()) {
//            log.warn("Cart is empty for user: {}", userEmail);
            throw new IllegalStateException("ไม่สามารถทำรายการได้เนื่องจากตะกร้าสินค้าว่าง");
        }

        // ✅ ตรวจสอบ stock ก่อนสั่งซื้อทุกรายการ
        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            int quantity = item.getQuantity();

            if (product == null) {
//                log.error("Product is null in cart for user: {}", userEmail);
                throw new IllegalStateException("ไม่พบข้อมูลสินค้าในตะกร้า");
            }

            if (!productService.isInStock(product.getId(), quantity)) {
//                log.warn("Insufficient stock for product: {} (ID: {})", product.getName(), product.getId());
                throw new IllegalStateException("สินค้าคงเหลือไม่เพียงพอ: " + product.getName());
            }
        }

        // สร้างคำสั่งซื้อใหม่
        Order order = new Order();
        order.setCustomerEmail(userEmail);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.Status.PENDING_PAYMENT);

        // แปลง CartItem เป็น OrderItem
        List<OrderItem> items = cartItems.stream().map(item -> {
            Product product = item.getProduct();
            int quantity = item.getQuantity();

            // เช็คสต็อกอีกครั้งก่อนหัก (แม้เช็คแล้วก่อนหน้า) เพื่อความปลอดภัย
            if (product.getStock() < quantity) {
//                log.warn("Not enough stock for product: {} (ID: {}) during final check", product.getName(), product.getId());
                throw new IllegalStateException("จำนวนสินค้าไม่พอ: " + product.getName());
            }

            // ✅ หัก stock + log แบบปลอดภัย
            productService.decreaseStock(product.getId(), quantity);

            OrderItem oi = new OrderItem();
            oi.setProduct(product);
            oi.setProductId(product.getId());
            oi.setProductName(item.getProductName());
            oi.setPrice(item.getPrice());
            oi.setQuantity(item.getQuantity());
            oi.setOrder(order);
            // คำนวณ total ของ OrderItem ก่อน save
            oi.setTotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));

            return oi;
        }).collect(Collectors.toList());

        // คำนวณราคาทั้งหมด
        BigDecimal total = items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // ตั้งค่าราคาสินค้ารวมในคำสั่งซื้อ
        order.setTotalAmount(total);
        order.setOrderItems(items);

        // เคลียร์ตะกร้าหลังการสั่งซื้อ
        cartService.clearCart(userEmail); // clear cart after checkout
//        log.info("Cleared cart for user: {}", userEmail);

        // บันทึกคำสั่งซื้อใหม่
        Order savedOrder = orderRepository.save(order);
//        log.info("Order saved with ID: {}", savedOrder.getId());

        return savedOrder;
    }

    public List<Order> getOrdersByEmail(Principal principal) {
        // ดึงอีเมลของผู้ใช้ที่ล็อกอินจาก Principal
        String userEmail = principal.getName();
        // ดึงคำสั่งซื้อจากฐานข้อมูลที่ตรงกับ userEmail
        return orderRepository.findByCustomerEmail(userEmail);
    }

    public List<Order> findByUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(u -> orderRepository.findByUser(u)) // ถ้า user ไม่เป็น null
                .orElseGet(Collections::emptyList); // ถ้า user เป็น null ให้ return empty list
    }

    public String getLocalizedStatus(Order.Status status, Locale locale) {
        return messageSource.getMessage("order.status." + status.name(), null, locale);
    }
}
