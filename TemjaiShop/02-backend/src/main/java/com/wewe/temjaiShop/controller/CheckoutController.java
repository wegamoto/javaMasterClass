package com.wewe.temjaiShop.controller;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import com.wewe.temjaiShop.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import com.wewe.temjaiShop.model.CartItem;
import com.wewe.temjaiShop.model.Order;
import com.wewe.temjaiShop.service.CartService;
import com.wewe.temjaiShop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/checkout")
public class CheckoutController {

    private CartService cartService;
    private OrderService orderService;

    @GetMapping
    public String showCheckoutPage(Model model, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        // String userEmail = extractEmailFromPrincipal(authentication);

        List<CartItem> cartItems = cartService.getCartItems(authentication);

        if (CollectionUtils.isEmpty(cartItems)) {
            model.addAttribute("error", "ไม่สามารถชำระเงินได้ เพราะตะกร้าสินค้าว่างเปล่า");
            return "redirect:/cart/view";
        }

        BigDecimal total = cartItems.stream()
                .map(CartItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        return "cart/view";
    }

    @PostMapping
    public String processCheckout(Authentication authentication, RedirectAttributes redirectAttributes) {
        if (authentication == null) {
//            log.warn("พยายาม checkout โดยไม่เข้าสู่ระบบ");
            redirectAttributes.addFlashAttribute("error", "กรุณาเข้าสู่ระบบก่อนทำรายการ");
            return "redirect:/login";
        }

        try {
            // ดึง userId จาก principal
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Long userId = userPrincipal.getId();

            Order order = orderService.createOrderFromCart(userId);

            return "redirect:/checkout/success";
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cart/view";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "เกิดข้อผิดพลาดขณะทำรายการ#3");
            return "redirect:/cart/view";
        }
    }

    private String extractEmailFromPrincipal(Authentication authentication) {
        if (authentication == null) {
            throw new IllegalArgumentException("Authentication object must not be null.");
        }

        if (authentication instanceof OAuth2AuthenticationToken oauth2Token) {
            OAuth2User oauth2User = oauth2Token.getPrincipal();
            Object emailAttr = oauth2User.getAttributes().get("email");

            if (emailAttr != null) {
                return emailAttr.toString();
            } else {
                throw new IllegalStateException("ไม่สามารถดึงอีเมลจากบัญชี OAuth2 ได้ (missing 'email' attribute)");
            }
        }

        // กรณี Local Login ซึ่งใช้ email เป็น username
        String name = authentication.getName();
        if (name == null || name.isBlank()) {
            throw new IllegalStateException("ไม่พบชื่อผู้ใช้ในข้อมูล Authentication");
        }

        return name;
    }

    @GetMapping("/success")
    public String showSuccessPage() {
        return "checkout-success";
    }
}
