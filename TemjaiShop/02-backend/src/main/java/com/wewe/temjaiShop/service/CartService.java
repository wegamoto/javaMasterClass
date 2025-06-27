package com.wewe.temjaiShop.service;

import com.wewe.temjaiShop.model.CartItem;
import com.wewe.temjaiShop.model.Order;
import com.wewe.temjaiShop.model.OrderItem;
import com.wewe.temjaiShop.model.Product;
import com.wewe.temjaiShop.model.User;
import com.wewe.temjaiShop.repository.CartItemRepository;
import com.wewe.temjaiShop.repository.OrderRepository;
import com.wewe.temjaiShop.repository.ProductRepository;
import com.wewe.temjaiShop.repository.UserRepository;
import com.wewe.temjaiShop.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private RetryTemplate retryTemplate;

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public CartService(CartItemRepository cartItemRepository,
                       UserRepository userRepository,
                       ProductRepository productRepository,
                       OrderRepository orderRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void addToCart(Long userId, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("จำนวนสินค้าต้องมากกว่า 0");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("ไม่พบผู้ใช้งาน"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("ไม่พบสินค้า"));

        // เพิ่มสินค้าลงในตะกร้าหรืออัปเดตจำนวนหากมีอยู่แล้ว
        cartItemRepository.upsertCartItem(
                product.getPrice(),
                product.getId(),
                product.getName(),
                quantity,
                user.getId()
        );
    }

    @Transactional
    public void addOrUpdateCartItem(Long productId, int quantity, String username) {
        User user = userRepository.findByUsernameOrEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = cartItemRepository.findByUserAndProduct(user, product)
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setUser(user);
                    newItem.setProduct(product);
                    newItem.setProductName(product.getName());
                    newItem.setPrice(product.getPrice());
                    return newItem;
                });

        item.setQuantity(item.getQuantity() + quantity);
        cartItemRepository.save(item);
    }

//    // เพิ่มสินค้าลงในตะกร้า
//    private void doAddToCart(String userEmail, Long productId, int quantityToAdd) {
//        // ดึง User และ Product
//        User user = userRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));
//
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
//
//        // 🔒 ใช้ Lock กัน race condition (เหมาะสำหรับระบบที่ต้องรองรับ concurrency)
//        Optional<CartItem> optionalItem = cartItemRepository.findWithLockByUserIdAndProductId(user.getId(), productId);
//
//        if (optionalItem.isPresent()) {
//            CartItem item = optionalItem.get();
//            item.setQuantity(item.getQuantity() + quantityToAdd);
//            cartItemRepository.save(item);
//        } else {
//            CartItem newItem = new CartItem();
//            newItem.setUser(user);
//            newItem.setProduct(product);
//            newItem.setQuantity(quantityToAdd);
//            newItem.setPrice(product.getPrice());
//            newItem.setProductName(product.getName());
//
//            cartItemRepository.save(newItem);
//        }
//    }
//
//    @Transactional
//    public void addToCartWithLock(Long userId, Long productId, int quantityToAdd) {
//        Optional<CartItem> optionalItem = cartItemRepository.findWithLockByUserIdAndProductId(userId, productId);
//
//        if (optionalItem.isPresent()) {
//            CartItem item = optionalItem.get();
//            item.setQuantity(item.getQuantity() + quantityToAdd);
//            cartItemRepository.save(item);
//        } else {
//            // load user and product entity before this
//            User user = userRepository.findById(userId).orElseThrow();
//            Product product = productRepository.findById(productId).orElseThrow();
//
//            CartItem newItem = new CartItem();
//            newItem.setUser(user);
//            newItem.setProduct(product);
//            newItem.setQuantity(quantityToAdd);
//            newItem.setPrice(product.getPrice());
//            newItem.setProductName(product.getName());
//
//            cartItemRepository.save(newItem);
//        }
//    }

    // นับจำนวนสินค้าในตะกร้าของผู้ใช้
    public int countItemsInCart(Long userId) {
        return cartItemRepository.countByUser_Id(userId);
    }

    // ดึงสินค้าในตะกร้าของผู้ใช้
    public List<CartItem> getCartItems(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        Long userId = principal.getId();

        return cartItemRepository.findByUserId(userId);
    }

    // เช็คเอาท์: สร้าง order แล้วลบรายการสินค้าจากตะกร้า
    @Transactional
    public Long checkout(Long userId) {
        // ดึง cart items ของผู้ใช้
        List<CartItem> cartItems = cartItemRepository.findByUser_Id(userId);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("ตะกร้าของคุณว่างเปล่า");
        }

        // ดึงข้อมูลผู้ใช้
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("ไม่พบผู้ใช้"));

        // สร้าง Order และ OrderItems จาก cartItems
        Order order = new Order();
        order.setUser(user);


        order.setOrderDate(LocalDateTime.now());

        order.setUserEmail(user.getEmail());       // ถ้ามีฟิลด์เก็บ userEmail
        // กำหนดสถานะ Order (ถ้ามี)
        order.setStatus(Order.Status.PENDING_PAYMENT); // หรือสถานะอื่นที่เหมาะสม

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());

            total = total.add(cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));

            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order.setTotal(total);

        orderRepository.save(order);
        cartItemRepository.deleteAll(cartItems);
        return order.getId();
    }

    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }

    public int getCartItemCount(Long userId) {
        return cartItemRepository.countByUser_Id(userId); // ใช้ user_id จาก foreign key
    }

    // อัพเดตจำนวนสินค้าในตะกร้า
    public void updateCartItem(Long productId, int quantity, String userEmail) {
        User user = userRepository.findByUsernameOrEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + userEmail));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new IllegalStateException("Cart item not found for productId: " + productId + " and user: " + userEmail));

        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
    }

    public void removeCartItem(Long cartItemId, String userEmail) {
        User user = userRepository.findByUsernameOrEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + userEmail));

        CartItem cartItem = cartItemRepository.findByIdAndUserId(cartItemId,user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission"));

        cartItemRepository.delete(cartItem);
    }

    @Transactional
    public void addProductToCart(Long productId, int quantity, Principal principal) {

        // ดึงอีเมลผู้ใช้จาก Principal
        String userEmail = principal.getName();

        // ดึงข้อมูลผู้ใช้
        User user = userRepository.findByUsernameOrEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ดึงข้อมูลสินค้า
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // ตรวจสอบว่าผู้ใช้นี้มี CartItem สำหรับสินค้านี้อยู่แล้วหรือไม่
        Optional<CartItem> existingItem = cartItemRepository.findByUserAndProduct(user, product);

        if (existingItem.isPresent()) {
            // ถ้ามี → เพิ่มจำนวน
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            // ถ้ายังไม่มี → สร้างใหม่
            CartItem newItem = new CartItem();
            newItem.setUser(user);  // 💡 ต้อง set User เพราะเราไม่มีตาราง cart
            newItem.setProduct(product);
            newItem.setProductName(product.getName());  // กรณีต้องการเก็บชื่อสินค้าแยก
            newItem.setPrice(product.getPrice());       // กรณีเก็บราคาล่าสุดแยก
            newItem.setQuantity(quantity);
            cartItemRepository.save(newItem);
        }
    }

    public List<CartItem> getCartItemsByUserId(Long userId) {
        return cartItemRepository.findByUserId(userId); // หรือแล้วแต่ logic ที่คุณใช้
    }

//    @jakarta.transaction.Transactional
//    public CartItem getCurrentCart(CartItemRepository cartItemRepository, Principal principal) {
//        // ดึงข้อมูล userId จาก Principal
//        String username = principal.getName();
//
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // ดึงข้อมูล Cart ที่เชื่อมโยงกับ user
//        Optional<Cart> cartOpt = cartRepository.findByUser(user);
//
//        if (cartOpt.isPresent()) {
//            return cartOpt.get();
//        } else {
//            // ถ้ายังไม่มี Cart ให้สร้างใหม่
//            Cart newCart = new Cart();
//            newCart.setUser(user);
//            return cartRepository.save(newCart);
//        }
//    }

}
