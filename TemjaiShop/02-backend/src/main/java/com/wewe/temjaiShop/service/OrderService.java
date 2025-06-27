package com.wewe.temjaiShop.service;

import com.wewe.temjaiShop.model.*;
import com.wewe.temjaiShop.repository.OrderRepository;
import com.wewe.temjaiShop.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
    public Order createOrderFromCart(Long userId) {

        // ดึงข้อมูล user ถ้าต้องการใช้ email
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ไม่พบผู้ใช้งาน"));

        String userEmail = user.getEmail(); // สำหรับเก็บใน order

        // ดึง cart ของ user จาก userId
        List<CartItem> cartItems = cartService.getCartItemsByUserId(userId);  // ✅ ใช้ userId ภายใน

        if (cartItems.isEmpty()) {
            throw new IllegalStateException("ตะกร้าสินค้าของผู้ใช้งานว่างเปล่า");
        }

        // ✅ ตรวจสอบ stock ก่อนสั่งซื้อทุกรายการ
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            if (product == null) {
                throw new IllegalStateException("Product ของ CartItem id=" + cartItem.getId() + " เป็น null");
            }
            int quantity = cartItem.getQuantity();
            if (!productService.isInStock(product.getId(), quantity)) {
                throw new IllegalStateException("สินค้าคงเหลือไม่เพียงพอ: " + product.getName());
            }
        }

        // สร้างคำสั่งซื้อใหม่
        Order order = new Order();
        order.setUser(user);
        order.setCustomerEmail(userEmail);
        order.setUserEmail(user.getEmail());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.Status.PENDING_PAYMENT);

        // แปลง CartItem เป็น OrderItem
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();

            // ตรวจสอบ stock อีกครั้ง (double check)
            if (product.getStockQuantity() < quantity) {
//                log.warn("Not enough stock for product: {} (ID: {}) during final check", product.getName(), product.getId());
                throw new IllegalStateException("จำนวนสินค้าไม่พอ: " + product.getName());
            }

            // ✅ หัก stock + log แบบปลอดภัย
            productService.decreaseStock(product.getId(), quantity);

            OrderItem oi = new OrderItem();
            oi.setProduct(product);
            oi.setProductName(product.getName());
            oi.setPrice(cartItem.getPrice());
            oi.setQuantity(cartItem.getQuantity());
            oi.setOrder(order);
            // คำนวณ total ของ OrderItem ก่อน save
            oi.setTotal(cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));

            return oi;
        }).collect(Collectors.toList());

        // คำนวณราคาทั้งหมด
        BigDecimal total = cartItems.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // ตั้งค่าราคาสินค้ารวมในคำสั่งซื้อ
        order.setTotalAmount(total);
        order.setOrderItems(orderItems);

        // เคลียร์ตะกร้าหลังการสั่งซื้อ
        cartService.clearCart(userId); // clear cart after checkout

        // บันทึกคำสั่งซื้อใหม่
        return orderRepository.save(order);
    }

    private String extractEmailFromPrincipal(Principal principal) {
        if (principal instanceof OAuth2AuthenticationToken oauth2Token) {
            OAuth2User oauth2User = oauth2Token.getPrincipal();
            Object emailAttr = oauth2User.getAttributes().get("email");
            if (emailAttr != null) {
                return emailAttr.toString();
            } else {
                throw new IllegalStateException("ไม่สามารถดึงอีเมลจากบัญชี OAuth2 ได้");
            }
        }

        // fallback: ใช้ชื่อผู้ใช้แบบ form login (username เป็น email)
        return principal.getName();
    }

    public List<Order> getOrdersByPrincipal(Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("Unauthenticated access");
        }

        String userEmail = extractEmailFromPrincipal(principal);
        if (userEmail == null || userEmail.isBlank()) {
            throw new UsernameNotFoundException("Invalid user email from Principal");
        }

        User user = userRepository.findByUsernameOrEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));

        return orderRepository.findByUserEmail(userEmail);
    }

    public List<Order> findOrdersByUsername(String username) {
        return orderRepository.findByUserEmail(username);  // ตรวจสอบชื่อเมธอด และการแมปใน Entity
    }


    public List<Order> getOrdersByEmail(Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("Unauthenticated access");
        }

        // ดึงอีเมลของผู้ใช้ที่ล็อกอินจาก Principal
        String userEmail = extractEmailFromPrincipal(principal);
        if (userEmail == null || userEmail.isBlank()) {
            throw new UsernameNotFoundException("Invalid user email from Principal");
        }

        // ดึง user จาก email
        User user = userRepository.findByUsernameOrEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));

        // ดึงคำสั่งซื้อจากฐานข้อมูลที่ตรงกับ โดยใช้ userId
        return orderRepository.findByUserEmail(userEmail);
    }

    public List<Order> findByUser(String username) {
        Optional<User> user = userRepository.findByUsernameOrEmail(username);
        return user.map(u -> orderRepository.findByUser(u)) // ถ้า user ไม่เป็น null
                .orElseGet(Collections::emptyList); // ถ้า user เป็น null ให้ return empty list
    }

    public String getLocalizedStatus(Order.Status status, Locale locale) {
        return messageSource.getMessage("order.status." + status.name(), null, locale);
    }

    public List<Order> getOrdersByUserEmail(String email) {
        // ดึง order ตาม userEmail
        return orderRepository.findByUserEmailOrderByCreatedAtDesc(email);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        // ดึง order ตาม userId
        return orderRepository.findByUserId(userId);
    }

    public List<Order> getOrdersByUser(User user) {
        // ดึง order ตาม user entity
        return orderRepository.findByUser(user);
    }

    public List<Order> getAllOrdersSortedByDateDesc() {
        return orderRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public Order findById(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        return optionalOrder.orElse(null); // หรือ throw exception ถ้าไม่พบ
    }

}
