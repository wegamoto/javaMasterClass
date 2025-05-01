package com.wewe.weweShop.service;

import com.wewe.weweShop.model.*;
import com.wewe.weweShop.model.enums.OrderStatus;
import com.wewe.weweShop.repository.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private CartRepository cartRepository;


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

        // หา Product จาก productId
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // หา cartItem เดิม ว่ามีอยู่หรือยัง
        CartItem existingCartItem = cartItemRepository.findByUserEmailAndProductId(userEmail, productId);

        if (existingCartItem != null) {
            // ถ้ามีสินค้าเดิมในตะกร้า => เพิ่ม quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
            cartItemRepository.save(existingCartItem);
        } else {
            // ถ้ายังไม่มี => สร้างใหม่
            CartItem cartItem = new CartItem();
            cartItem.setUserEmail(userEmail);
            cartItem.setProductId(productId);
            cartItem.setProductName(product.getName());
            cartItem.setPrice(BigDecimal.valueOf(product.getPrice()));
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
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

    @Transactional
    public Long checkout(Principal principal) {
        String userEmail = principal.getName();

        // 1. ดึงสินค้าทั้งหมดในตะกร้า
        List<CartItem> cartItems = cartItemRepository.findByUserEmail(userEmail);

        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cannot checkout an empty cart.");
        }

        // 3. เพิ่ม OrderItem สำหรับแต่ละสินค้า
        Order order = new Order();
        order.setUser(userEmail);
        order.setOrderDate(LocalDateTime.now());
        order.setCustomerEmail(userEmail);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING_PAYMENT);

        order = orderRepository.save(order); // save ก่อน เพื่อให้ได้ orderId

        // คำนวณ total
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();


        // 4. เพิ่ม OrderItem สำหรับแต่ละสินค้า

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setProductName(cartItem.getProductName());
            orderItem.setPrice(BigDecimal.valueOf(cartItem.getProduct().getPrice()));
            orderItem.setQuantity(cartItem.getQuantity());

            // คำนวณ total สำหรับ OrderItem และตั้งค่า total
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
    public void addProductToCart(Long productId, int quantity) {
        Cart cart = getCurrentCart(cartRepository);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // เช็คก่อนว่ามีสินค้านี้ในตะกร้าแล้วหรือยัง
        Optional<CartItem> existingItemOpt = cartItemRepository.findByCartAndProduct(cart, product);

        if (existingItemOpt.isPresent()) {
            // ถ้ามีอยู่แล้ว → เพิ่มจำนวน
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemRepository.save(existingItem);
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
    public Cart getCurrentCart(CartRepository cartRepository) {
        Long userId = getCurrentUserId();

        Optional<Cart> cartOpt = cartRepository.findByUserId(userId);

        if (cartOpt.isPresent()) {
            return cartOpt.get();
        } else {
            // ถ้าไม่มี Cart ให้สร้างใหม่
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            return cartRepository.save(newCart);
        }
    }

    private Long getCurrentUserId() {
        // Mock user id = 1L (ทำเป็นค่าคงที่ไว้ใช้งานก่อน)
        return 1L;
    }

}
