package com.wewe.temjaiShop.controller;

import com.wewe.temjaiShop.model.*;
import com.wewe.temjaiShop.repository.CartItemRepository;
import com.wewe.temjaiShop.repository.UserRepository;
import com.wewe.temjaiShop.security.UserPrincipal;
import com.wewe.temjaiShop.security.oauth.CustomOAuth2User;
import com.wewe.temjaiShop.service.CartItemService;
import com.wewe.temjaiShop.service.CartService;
import com.wewe.temjaiShop.service.OrderService;
import com.wewe.temjaiShop.service.ProductService;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final CartItemRepository cartItemRepository;
    private final OrderService orderService;
    private final CartItemService cartItemService;
    private final UserRepository userRepository;
    private final ProductService productService;

    @Autowired
    public CartController(CartService cartService,
                          CartItemRepository cartItemRepository,
                          OrderService orderService,
                          CartItemService cartItemService,
                          UserRepository userRepository,
                          ProductService productService) {
        this.cartService = cartService;
        this.cartItemRepository = cartItemRepository;
        this.orderService = orderService;
        this.cartItemService = cartItemService;
        this.userRepository = userRepository;
        this.productService = productService;
    }

    /**
     * แสดงหน้าตะกร้าสินค้า (GET /cart/view)
     */
    @GetMapping("/view")
    public String viewCart(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        // String userEmail = extractEmailFromPrincipal(principal);
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userPrincipal.getId();
        System.out.println("***** Current userId: " + userId);  // พิมพ์ userEmail ออกมาที่คอนโซล

        if (userId == null) {
            return "redirect:/login";
        }

        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        System.out.println("cartItems size = " + cartItems.size());

        model.addAttribute("cartItems", cartItems);

        int totalItems = cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        // คำนวณราคารวมทั้งหมด
        BigDecimal total = cartItems.stream()
                .map(i -> (i.getPrice() != null ? i.getPrice() : BigDecimal.ZERO)
                        .multiply(BigDecimal.valueOf(i.getQuantity() != null ? i.getQuantity() : 0)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartItemCount", totalItems); //ใช้ใน Navbar
        model.addAttribute("total", total);

        return "cart";
    }

    /**
     * เพิ่มสินค้าเข้าตะกร้า (POST /cart/add)
     */
    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam("quantity") @Min(1) int quantity,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();

        try {
            cartService.addToCart(userId, productId, quantity);
            redirectAttributes.addFlashAttribute("success", "เพิ่มสินค้าในตะกร้าเรียบร้อยแล้ว");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "เกิดข้อผิดพลาดขณะเพิ่มสินค้าในตะกร้า");
        }

        return "redirect:/cart/view";
    }

    /**
     * ดึงจำนวนสินค้าทั้งหมดในตะกร้า (AJAX GET /cart/count)
     */
    @GetMapping("/count")
    @ResponseBody
    public Integer getCartItemCount(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return 0;
        }

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        Long userId = principal.getId();

        return cartService.getCartItemCount(userId);
    }

    /**
     * อัปเดตจำนวนสินค้าภายในตะกร้า (POST /cart/update)
     */
    @PostMapping("/update")
    public String updateCartItem(@RequestParam("productId") Long productId,
                                 @RequestParam("quantity") @Min(1) int quantity,
                                 Authentication authentication) {
        String email = extractUserEmail(authentication);
        cartService.updateCartItem(productId, quantity, email);
        return "redirect:/cart/view";
    }

    /**
     * ลบสินค้าจากตะกร้า (POST /cart/remove)
     */
    @PostMapping("/remove")
    public String removeCartItem(@RequestParam("cartItemId") Long cartItemId,
                                 Authentication authentication) {
        String email = extractUserEmail(authentication);
        cartService.removeCartItem(cartItemId, email);
        return "redirect:/cart/view";
    }

    /**
     * เคลียร์ตะกร้าสินค้าทั้งหมด (POST /cart/clear)
     */
    @PostMapping("/clear")
    public String clearCart(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();

        cartService.clearCart(userId);
        return "redirect:/cart/view";
    }

    /**
     * ทำรายการชำระเงิน (POST /cart/checkout)
     */
    @PostMapping("/checkout")
    public String processCheckout(Authentication authentication, RedirectAttributes redirectAttributes) {
        if (authentication == null || !authentication.isAuthenticated()) {
            redirectAttributes.addFlashAttribute("error", "กรุณาเข้าสู่ระบบก่อนทำรายการ");
            return "redirect:/login";
        }

        // ✅ ใช้ UserPrincipal หลังจากรวมแล้ว
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        Long userId = userDetails.getId();
        System.out.print("**1** User ID : " + userId);

        try {
            Order order = orderService.createOrderFromCart(userId);
            return "redirect:/checkout/success?orderId=" + order.getId();
        } catch (IllegalStateException e) {
            // เช่น ตะกร้าว่าง, สินค้าไม่พอ
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cart/view";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "เกิดข้อผิดพลาดขณะทำรายการ#2");
            return "redirect:/cart/view";
        }
    }

    /*
     * ฟังก์ชันช่วยดึงอีเมลจาก Principal (รองรับ UserDetails และ OAuth2User)
     */
    private String extractEmailFromPrincipal(Principal principal) {
        if (principal instanceof Authentication authentication) {
            Object principalObj = authentication.getPrincipal();

            if (principalObj instanceof UserDetails userDetails) {
                return userDetails.getUsername();
            }

            if (principalObj instanceof OAuth2User oAuth2User) {
                String email = oAuth2User.getAttribute("email");
                if (email != null) return email;

                Object altEmail = oAuth2User.getAttribute("preferred_username");
                return altEmail != null ? altEmail.toString() : null;
            }
        }
        return null;
    }

    /*
     * ฟังก์ชันช่วยดึงอีเมลจาก Authentication (รองรับ UserDetails และ OAuth2User)
     */
    private String extractUserEmail(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();

        } else if (principal instanceof OAuth2User oauth2User) {
            Object emailAttr = oauth2User.getAttributes().get("email");
            return emailAttr != null ? emailAttr.toString() : null;

        } else if (principal instanceof String strPrincipal) {
            if (!"anonymousUser".equals(strPrincipal)) {
                return strPrincipal;
            }
            return null;
        } else {
            return null;
        }
    }

    @ModelAttribute("cartItemCount")
    public int cartItemCount(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return 0;
        }

        String usernameOrEmail = null;

        Object principal = authentication.getPrincipal();
        if (principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
            usernameOrEmail = userDetails.getUsername();
        } else if (principal instanceof CustomOAuth2User oauthUser) {
            usernameOrEmail = oauthUser.getEmail();
        }

        if (usernameOrEmail == null) {
            return 0;
        }

        return userRepository.findByUsernameOrEmail(usernameOrEmail)
                .map(user -> cartItemRepository.countByUser_Id(user.getId()))
                .orElse(0);
    }
}
