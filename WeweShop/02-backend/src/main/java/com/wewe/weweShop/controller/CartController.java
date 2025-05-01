package com.wewe.weweShop.controller;

import com.wewe.weweShop.dto.CartItemForm;
import com.wewe.weweShop.model.CartItem;
import com.wewe.weweShop.model.Order;
import com.wewe.weweShop.model.OrderItem;
import com.wewe.weweShop.repository.CartItemRepository;
import com.wewe.weweShop.service.CartService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private final CartService cartService;

    @Autowired
    private final CartItemRepository cartItemRepository = null;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // สำหรับแสดงข้อมูลในตะกร้า (GET /cart)
    @GetMapping
    public String showCart(Model model, @RequestParam("userEmail") String userEmail) {
        List<CartItem> cartItems = cartService.getCartItems(userEmail);
        model.addAttribute("cartItems", cartItems);
        return "cart";  // ชื่อไฟล์ที่ใช้แสดงผล (cart.html)
    }

    // ✅ หน้าตะกร้าใหม่
    @GetMapping("/view")
    public String viewCart(Model model, Principal principal) {

        if (principal == null) {
            return "redirect:/login"; // เปลี่ยนเส้นทางไปที่หน้าล็อกอิน
        }

        // ดึง userEmail จาก principal
        String userEmail = principal.getName(); // ดึง email จาก principal

        // เรียกใช้ cartService เพื่อดึงข้อมูลรายการสินค้าในตะกร้า
        List<CartItem> cartItems = cartService.getCartItems(userEmail);

        // คำนวณราคาทั้งหมดในตะกร้า
        BigDecimal total = cartItems.stream()
                .map(item -> {
                    BigDecimal price = item.getPrice() != null ? item.getPrice() : BigDecimal.ZERO;
                    int quantity = item.getQuantity() != null ? item.getQuantity() : 0;
                    return price.multiply(BigDecimal.valueOf(quantity));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        return "cart";
    }

    // ✅ เพิ่มสินค้าจาก RequestParam
    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam("quantity") @Min(1) int quantity,
                            Principal principal) {

        // ตรวจสอบว่า principal (หรือ user) มีค่าอยู่หรือไม่
        if (principal == null || principal.getName() == null || principal.getName().isEmpty()) {
            throw new IllegalArgumentException("User email cannot be null or empty.");
        }

        String userEmail = principal.getName(); // ดึง email จาก principal

        cartService.addToCart(userEmail,productId, quantity);
        return "redirect:/cart/view";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam("quantity") int quantity,
                            RedirectAttributes redirectAttributes) {
        cartService.addProductToCart(productId, quantity);
        redirectAttributes.addFlashAttribute("message", "เพิ่มสินค้าสำเร็จ!");
        return "redirect:/recommendations"; // หรือ redirect:/cart ถ้าอยากพาไปหน้าตะกร้า
    }

    // ✅ เพิ่มสินค้าจาก Form
    @PostMapping("/addForm")
    public String addToCart(@ModelAttribute @Valid CartItemForm form,
                            BindingResult result,
                            String userEmail) {
        if (result.hasErrors()) {
            return "redirect:/products?error=invalid";
        }
        cartService.addToCart(userEmail, form.getProductId(), form.getQuantity());
        return "redirect:/cart/view"; // (แก้จาก /view เป็น /cart/view)
    }

    // ✅ อัปเดตสินค้าในตะกร้า
    @PostMapping("/update")
    public String updateCartItem(@RequestParam("productId") Long productId,
                                 @RequestParam("quantity") @Min(1) int quantity) {
        cartService.updateCartItem(productId, quantity);
        return "redirect:/cart/view";
    }

    // ✅ ลบสินค้า
    @PostMapping("/remove")
    public String removeCartItem(@RequestParam("productId") Long productId,
                                 Principal principal) {
        cartService.removeCartItem(principal, productId);
        return "redirect:/cart/view";
    }

    // ✅ เคลียร์ตะกร้า
    @PostMapping("/clear")
    public String clearCart(Principal principal) {
        String userEmail = principal.getName();
        cartService.clearCart(userEmail);
        return "redirect:/cart/view";
    }

    @PostMapping("/checkout")
    public String checkout(Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        // เรียก checkout ผ่าน service และรับ orderId กลับมา
        Long orderId = cartService.checkout(principal);

        // Redirect ไปหน้า success พร้อมส่ง orderId ไปด้วย
        return "redirect:/orders/success?orderId=" + orderId;
    }



}
