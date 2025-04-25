package com.wewe.weweShop.controller;

import com.wewe.weweShop.model.CartItem;
import com.wewe.weweShop.service.CartService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    private String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("cartItems", cartService.getCart(getCurrentUserEmail()));
        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId, @RequestParam String name,
                            @RequestParam double price, @RequestParam int quantity) {
        CartItem item = new CartItem();
        item.setProductId(productId);
        item.setProductName(name);
        item.setPrice(price);
        item.setQuantity(quantity);

        cartService.addToCart(getCurrentUserEmail(), item);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long productId) {
        cartService.removeFromCart(getCurrentUserEmail(), productId);
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateQuantity(@RequestParam Long productId, @RequestParam int quantity) {
        cartService.updateQuantity(getCurrentUserEmail(), productId, quantity);
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkoutPage(Model model) {
        List<CartItem> items = cartService.getCart(getCurrentUserEmail());
        double total = items.stream().mapToDouble(CartItem::getTotal).sum();
        model.addAttribute("cartItems", items);
        model.addAttribute("total", total);
        return "checkout";
    }

    @PostMapping("/checkout/complete")
    public String completeCheckout() {
        cartService.clearCart(getCurrentUserEmail());
        return "redirect:/cart?checkoutSuccess";
    }

}

