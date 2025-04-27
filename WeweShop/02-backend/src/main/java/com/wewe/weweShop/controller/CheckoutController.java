package com.wewe.weweShop.controller;

import com.wewe.weweShop.model.CartItem;
import com.wewe.weweShop.model.Order;
import com.wewe.weweShop.service.CartService;
import com.wewe.weweShop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/checkout")
public class CheckoutController {

    private final CartService cartService;
    private final OrderService orderService;

    @GetMapping
    public String showCheckoutPage(Model model, String userEmail) {
//        String userEmail = cartService.getCurrentUserEmail(principal);
        List<CartItem> cartItems = cartService.getCartItems(userEmail);

        BigDecimal total = cartItems.stream()
                .map(CartItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        return "checkout";
    }

    @PostMapping
    public String processCheckout(Principal principal) {
        String userEmail = cartService.getCurrentUserEmail(principal);
        String email = cartService.getCurrentUserEmail(principal);
        Order order = orderService.createOrderFromCart(userEmail);

        cartService.clearCart(userEmail); // clear cart after checkout
        return "redirect:/checkout/success";
    }

    @GetMapping("/success")
    public String showSuccessPage() {
        return "checkout-success";
    }
}
