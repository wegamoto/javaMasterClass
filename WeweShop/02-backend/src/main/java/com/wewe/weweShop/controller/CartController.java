package com.wewe.weweShop.controller;

import com.wewe.weweShop.model.CartItem;
import com.wewe.weweShop.service.CartService;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart/showCart")
    public String showCartPage(Model model, @RequestParam("userEmail") String userEmail) {
        List<CartItem> cartItems = cartService.getCartItems(userEmail);
        model.addAttribute("cartItems", cartItems);
        return "cart";
    }

    @GetMapping
    public String showCart(Model model) {
        String userEmail = cartService.getCurrentUserEmail();
        List<CartItem> cartItems = cartService.getCartItems(userEmail);
        BigDecimal total = cartItems.stream()
                .map(CartItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam("quantity") @Min(1) int quantity) {
        String userEmail = cartService.getCurrentUserEmail();  // ดึงอีเมลจาก security context
        cartService.addToCart(userEmail, productId, quantity);
        return "redirect:/cart"; // หรือ redirect ไปหน้าตะกร้า
    }

    @PostMapping("/update")
    public String updateCartItem(@RequestParam("productId") Long productId,
                                 @RequestParam("quantity") @Min(1) int quantity) {
        cartService.updateCartItem(productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeCartItem(@RequestParam("productId") Long productId) {
        cartService.removeCartItem(productId);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart() {
        String userEmail = cartService.getCurrentUserEmail();
        cartService.clearCart(userEmail);
        return "redirect:/cart";
    }
}
