package com.wewe.weweShop.controller;

import com.wewe.weweShop.model.Order;
import com.wewe.weweShop.repository.OrderRepository;
import com.wewe.weweShop.service.CartService;
import com.wewe.weweShop.service.CurrencyService;
import com.wewe.weweShop.service.OrderService;
import com.wewe.weweShop.util.NumberToWords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.text.DecimalFormat;
import java.util.List;

import static org.thymeleaf.util.NumberUtils.formatCurrency;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final CurrencyService currencyService;

    @Autowired
    private final OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService; // เพิ่ม CartService

    @Autowired
    public OrderController(CurrencyService currencyService, OrderService orderService) {
        this.currencyService = currencyService;
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public String viewOrders(Model model, Authentication authentication) {
        String email = authentication.getName();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        List<Order> orders;
        if (isAdmin) {
            orders = orderService.getAllOrders(); // แสดงทุกคำสั่งซื้อถ้าเป็นแอดมิน
        } else {
            orders = orderService.getOrdersByEmail(email); // เฉพาะของตัวเองถ้าเป็น user
        }

        model.addAttribute("orders", orders);
        model.addAttribute("isAdmin", isAdmin);
        return "orders/list";  // ไปยัง orders.html
    }

//    @PostMapping("/checkout")
//    public String checkout(String userEmail, Model model, Principal principal) {
//        if (userEmail == null) {
//            return "redirect:/login"; // หากผู้ใช้งานไม่ได้ล็อกอิน ให้เปลี่ยนเส้นทางไปที่หน้าล็อกอิน
//        }
//
//        // ดึง userEmail จาก principal
//        userEmail = principal.getName(); // principal.getName() คือ email ของผู้ใช้ที่ล็อกอิน
//
//        // ทำการ checkout และส่งไปยัง success page
//        Long orderId = cartService.checkout(userEmail); // ใช้ userEmail ในการ checkout
//        model.addAttribute("orderId", orderId);
//        return "redirect:/orders/success?orderId=" + orderId;
//    }

    @GetMapping("/success")
    public String successPage(@RequestParam("orderId") Long orderId, Model model) {

        //ดึง Order  จากฐานข้อมูล
        model.addAttribute("orderId", orderId);
        // ถ้าคุณต้องการดึงข้อมูลของ order ด้วย orderId
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalStateException("Order not found"));

        // ใช้ CurrencyService แปลงจำนวนเงินให้เป็นรูปแบบที่มีคอมม่า
        String formattedAmount = currencyService.formatCurrency(order.getTotalAmount());

        // แปลงจำนวนเงินทั้งหมดเป็นคำในภาษาไทย
        String totalAmountInWords = NumberToWords.convertToThaiCurrency(order.getTotalAmount().doubleValue());


        model.addAttribute("order", order);
        model.addAttribute("orderId", orderId);
        model.addAttribute("totalAmountInWords", totalAmountInWords); // ส่งค่าไปยัง Thymeleaf

        return "orders/success";  // แน่ใจว่าไฟล์ success.html อยู่ในโฟลเดอร์ resources/templates/orders
    }

        // 🧍 สำหรับผู้ใช้ทั่วไป
    @GetMapping("/user/orders")
    public String viewUserOrders(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // ใช้ email หรือ username
        List<Order> orders = orderService.getOrdersByEmail(email);
        model.addAttribute("orders", orders);
        return "orders"; // กลับไปยัง orders.html
    }

//    // 🔎 รายละเอียดของออร์เดอร์สำหรับผู้ใช้
//    @GetMapping("/user/orders/{id}")
//    public String viewOrderDetail(@PathVariable Long id, Model model) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String email = auth.getName();
//
//        Order order = orderService.getOrderByIdAndEmail(id, email);
//        model.addAttribute("order", order);
//        return "order-detail"; // กลับไปยัง order-detail.html
//    }

    // 👑 สำหรับแอดมินดูรายการทั้งหมด
    @GetMapping("/admin/orders")
    public String viewOrders(Model model) {
        List<Order> orders = orderRepository.findAll();
        model.addAttribute("orders", orders);
        return "order-list"; // กลับไปยัง order-list.html
    }
}
