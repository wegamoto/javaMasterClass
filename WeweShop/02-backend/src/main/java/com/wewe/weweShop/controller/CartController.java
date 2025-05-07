package com.wewe.weweShop.controller;

import com.wewe.weweShop.dto.CartItemForm;
import com.wewe.weweShop.model.CartItem;
import com.wewe.weweShop.model.Order;
import com.wewe.weweShop.model.OrderItem;
import com.wewe.weweShop.repository.CartItemRepository;
import com.wewe.weweShop.service.CartItemService;
import com.wewe.weweShop.service.CartService;
import com.wewe.weweShop.service.OrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private final CartService cartService;

    @Autowired
    private final CartItemRepository cartItemRepository = null;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Method สำหรับแสดงตะกร้าสินค้า
    @GetMapping("/cart/view")
    public String viewCart(Model model) {
        // เรียกคำนวณจำนวนสินค้าทั้งหมดในตะกร้า
        Integer cartItemCount = cartItemService.getTotalQuantityInCart();

        // ส่งจำนวนสินค้าผ่าน model ไปที่หน้าจอ
        model.addAttribute("cartItemCount", cartItemCount);

        return "cart/view"; // ชื่อ view ที่จะส่งกลับ
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

    @GetMapping("/cart/count")
    @ResponseBody
    public Integer getCartItemCount(Principal principal) {
        String email = principal.getName(); // ถ้า login แล้ว principal คือ email
        return cartService.getCartItemCount(email);
    }


    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam("quantity") int quantity,
                            Principal principal, // ใช้ Principal เพื่อดึงข้อมูลผู้ใช้งาน
                            RedirectAttributes redirectAttributes) {

        // เรียกใช้ service เพื่อเพิ่มสินค้าลงในตะกร้า
        cartService.addProductToCart(productId, quantity, principal);

        // เพิ่มข้อความแจ้งเตือนเมื่อสำเร็จ
        redirectAttributes.addFlashAttribute("message", "เพิ่มสินค้าสำเร็จ!");

        // Redirect ไปที่หน้าที่ต้องการ เช่น หน้ารายการแนะนำ หรือ หน้าตะกร้า
        return "redirect:/recommendations"; // หรือ redirect:/cart พาไปหน้าตะกร้า
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

//    @PostMapping("/checkout")
//    public String checkout(Principal principal) {
//        if (principal == null) {
//            return "redirect:/login";
//        }
//
//        // เรียก checkout ผ่าน service และรับ orderId,totalAmount กลับมา
//        Long orderId = cartService.checkout(principal);
//        Double totalAmount = cartService.getTotalAmount(principal);
//
//        // Redirect ไปหน้า success พร้อมส่ง orderId ไปด้วย
//        return "redirect:/orders/success?orderId=" + orderId + "&totalAmount=" + totalAmount;
//    }

    @PostMapping("/checkout")
    public String processCheckout(Principal principal, RedirectAttributes redirectAttributes) {
        if (principal == null) {
            log.warn("Checkout ล้มเหลว: ผู้ใช้ยังไม่ได้เข้าสู่ระบบ");
            redirectAttributes.addFlashAttribute("error", "กรุณาเข้าสู่ระบบก่อนทำรายการ");
            return "redirect:/login";
        }

        String userEmail = principal.getName();
        log.info("เริ่ม checkout สำหรับผู้ใช้: {}", userEmail);

        try {
            Order order = orderService.createOrderFromCart(principal);
            log.info("สร้างคำสั่งซื้อสำเร็จ: Order ID = {}, ผู้ใช้ = {}", order.getId(), userEmail);

            cartService.clearCart(userEmail);
            log.info("ล้างตะกร้าสินค้าของผู้ใช้ {} สำเร็จ", userEmail);

            return "redirect:/checkout/success";

        } catch (IllegalStateException e) {
            // 🟡 ถ้าตะกร้าว่าง ให้อยู่ที่หน้าเดิม
            log.warn("Checkout ไม่สำเร็จ (เช่น ตะกร้าว่าง): {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cart/view"; // ❗ redirect ไปที่ cart/view พร้อม Flash message
        } catch (Exception e) {
            log.error("เกิดข้อผิดพลาดขณะทำรายการ checkout ของผู้ใช้ {}", userEmail, e);
            redirectAttributes.addFlashAttribute("error", "เกิดข้อผิดพลาดขณะทำรายการ");
            return "redirect:/cart/view";
        }
    }

}
