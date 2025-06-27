package com.wewe.weweShop.controller;

import lombok.extern.slf4j.Slf4j;
import com.wewe.weweShop.model.CartItem;
import com.wewe.weweShop.model.Order;
import com.wewe.weweShop.service.CartService;
import com.wewe.weweShop.service.OrderService;
import lombok.RequiredArgsConstructor;
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
    public String showCheckoutPage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String userEmail = principal.getName();

        List<CartItem> cartItems = cartService.getCartItems(userEmail);

        if (cartItems == null || cartItems.isEmpty()) {
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
    public String processCheckout(Principal principal, RedirectAttributes redirectAttributes) {
        if (principal == null) {
//            log.warn("พยายาม checkout โดยไม่เข้าสู่ระบบ");
            redirectAttributes.addFlashAttribute("error", "กรุณาเข้าสู่ระบบก่อนทำรายการ");
            return "redirect:/login";
        }

        try {
            String userEmail = principal.getName();
//            log.info("เริ่มกระบวนการ checkout ของผู้ใช้: {}", userEmail);

            Order order = orderService.createOrderFromCart(principal);

//            log.info("สร้างคำสั่งซื้อสำเร็จ: Order ID = {}, Email = {}", order.getId(), userEmail);
            return "redirect:/checkout/success";
        } catch (IllegalStateException e) {
//            log.warn("Checkout ล้มเหลว (เช่น ตะกร้าว่าง): {}", e.getMessage());
            // เช่น ตะกร้าว่าง
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cart/view";
        } catch (Exception e) {
            // กรณี Error อื่นๆ
//            log.error("เกิดข้อผิดพลาดขณะทำ checkout", e);
            redirectAttributes.addFlashAttribute("error", "เกิดข้อผิดพลาดขณะทำรายการ");
            return "redirect:/cart/view";
        }
    }

    @GetMapping("/success")
    public String showSuccessPage() {
        return "checkout-success";
    }
}
