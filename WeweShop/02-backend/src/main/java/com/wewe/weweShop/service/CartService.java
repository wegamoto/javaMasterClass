package com.wewe.weweShop.service;

import com.wewe.weweShop.model.*;
import com.wewe.weweShop.repository.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserService userService;

    public int countItemsInCart(Long userId) {
        return 0;
    }

    @Autowired
    private CartRepository cartRepository;



    public Integer getCartItemCount(String email) {
        Integer count = cartItemRepository.sumQuantityByUserEmail(email);
        return count != null ? count : 0;
    }

    public String getCurrentUserEmail(Principal principal) {
        return principal != null ? principal.getName() : SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public List<CartItem> getCartItems(String userEmail) {
        return cartItemRepository.findByUserEmail(userEmail);
    }

    // เพิ่มสินค้าลงตะกร้าง
    @Transactional
    public void addToCart(String userEmail, Long productId, int quantity) {

        // ตรวจสอบว่า userEmail ไม่เป็น null
        if (userEmail == null || userEmail.isEmpty()) {
            throw new IllegalArgumentException("User email cannot be null or empty.");
        }

        // ดึง User จาก email
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // หา Product จาก productId
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // หา Cart ที่เชื่อมโยงกับ User
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });


        // หา CartItem เดิม ว่ามีอยู่หรือยัง
        // ✅ ใช้ findByCartAndProduct ที่เราสร้างไว้
        Optional<CartItem> existingCartItem = cartItemRepository.findByCartAndProduct(cart, product);

        if (existingCartItem.isPresent()) {
            // ถ้ามีสินค้าเดิมในตะกร้า => เพิ่ม quantity
            CartItem item = existingCartItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            // ถ้ายังไม่มี => สร้างใหม่
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cartItemRepository.save(newItem);
        }
    }

    public void addOrUpdateCartItem(String userEmail, Long productId, int quantity) {
        // เขียนโค้ด หรือปล่อยว่างไว้ก็ได้ถ้ายังไม่ทำ
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    // อัปเดตจำนวนสินค้าในตะกร้า
    @Transactional
    public void updateCartItemQuantity(Long cartItemId, int newQuantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("CartItem not found"));

        if (newQuantity <= 0) {
            cartItemRepository.deleteById(cartItemId); // ลบถ้าจำนวนเหลือ 0
        } else {
            cartItem.setQuantity(newQuantity);
            cartItemRepository.save(cartItem);
        }
    }

    @Transactional
    public void removeCartItem(Principal principal, Long productId) {
        String userEmail = principal.getName();
        CartItem cartItem = cartItemRepository.findByUserEmailAndProductId(userEmail, productId);
        if (cartItem != null) {
            cartItemRepository.delete(cartItem);
        }
    }

    // ลบสินค้าทั้งหมดในตะกร้าของ user
    @Transactional
    public void clearCart(String userEmail) {
        cartItemRepository.deleteByUserEmail(userEmail);
    }

    public BigDecimal getTotalAmount(Principal principal) {

        BigDecimal totalAmount = BigDecimal.ZERO;  // เริ่มต้นที่ 0
        // ดึงข้อมูลตะกร้าของผู้ใช้
        Cart cart = getOrCreateCartByPrincipal(principal);

        // ลูปผ่านรายการสินค้าที่อยู่ในตะกร้า
        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();

            // คำนวณราคาสินค้า * จำนวน โดยใช้ BigDecimal
            BigDecimal price = product.getPrice();  // ได้ค่าเป็น BigDecimal
            BigDecimal quantity = new BigDecimal(cartItem.getQuantity());  // แปลงจำนวนสินค้าที่เป็น int ให้เป็น BigDecimal

            totalAmount = totalAmount.add(price.multiply(quantity));  // คำนวณราคาสินค้า * จำนวน
        }

        return totalAmount;
    }

    @Transactional
    public Long checkout(Principal principal) {

        if (principal == null) {
            throw new IllegalArgumentException("User must be authenticated to checkout.");
        }

        String userEmail = principal.getName();

        // 1. ดึงสินค้าทั้งหมดในตะกร้า
        List<CartItem> cartItems = cartItemRepository.findByUserEmail(userEmail);

        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalStateException("Cannot checkout an empty cart.");
        }

        // 3. เพิ่ม OrderItem สำหรับแต่ละสินค้า
        Order order = new Order();
        order.setUserEmail(userEmail);
        order.setOrderDate(LocalDateTime.now());
        order.setCustomerEmail(userEmail);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(Order.Status.PENDING_PAYMENT);

        order = orderRepository.save(order); // save ก่อน เพื่อให้ได้ orderId

        // คำนวณ totalAmount
        BigDecimal totalAmount = BigDecimal.ZERO; // เซ็ตค่าเริ่มต้น 0

        // 4. เพิ่ม OrderItem สำหรับแต่ละสินค้า
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setProductName(cartItem.getProductName());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());

            // คำนวณ total สำหรับแต่ละ OrderItem
            BigDecimal itemTotal = orderItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            orderItem.setTotal(itemTotal); // ตั้งค่า total ให้กับ OrderItem

            // คำนวน totalAmount ของ Order
            totalAmount = totalAmount.add(itemTotal);
            orderItems.add(orderItem);

        }
        // ตั้งค่า totalAmount ของ Order
        order.setTotalAmount(totalAmount); //ตั้งค่า totalAmount สำหรับ Order ก่อนบันทึก
        order.setTotal(totalAmount);

        // บันทึก Order และ OrderItems
        order = orderRepository.save(order); // save ก่อน เพื่อให้ได้ orderId
        orderItemRepository.saveAll(orderItems);

        // 5. ล้างตะกร้า
        cartItemRepository.deleteByUserEmail(userEmail);

        // Return Order ID
        return order.getId();
    }

    public void updateCartItem(Long productId, @Min(1) int quantity) {
    }

    @Transactional
    public void addProductToCart(Long productId, int quantity, Principal principal) {

        // ดึงข้อมูลตะกร้าของผู้ใช้จาก Principal
        Cart cart = getCurrentCart(cartRepository, principal);

        // ดึงข้อมูลสินค้า
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // เช็คก่อนว่ามีสินค้านี้ในตะกร้าแล้วหรือยัง
        Optional<CartItem> existingItem = cartItemRepository.findByCartAndProduct(cart, product);

        if (existingItem.isPresent()) {
            // ถ้ามีอยู่แล้ว → เพิ่มจำนวน
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            // ถ้ายังไม่มี → สร้างใหม่
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cartItemRepository.save(newItem);
        }
    }

    @Transactional
    public Cart getCurrentCart(CartRepository cartRepository, Principal principal) {
        // ดึงข้อมูล userId จาก Principal
        String username = principal.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ดึงข้อมูล Cart ที่เชื่อมโยงกับ user
        Optional<Cart> cartOpt = cartRepository.findByUser(user);

        if (cartOpt.isPresent()) {
            return cartOpt.get();
        } else {
            // ถ้ายังไม่มี Cart ให้สร้างใหม่
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        }
    }

    private Long getCurrentUserId() {
        // Mock user id = 1L (ทำเป็นค่าคงที่ไว้ใช้งานก่อน)
        return 1L;
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName(); // คืนค่า username ปัจจุบัน
        }
        throw new RuntimeException("User is not authenticated");
    }

    public Cart getOrCreateCartByPrincipal(Principal principal) {
        String email = principal.getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }


}
