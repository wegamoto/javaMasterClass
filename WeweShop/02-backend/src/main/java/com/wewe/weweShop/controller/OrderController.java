package com.wewe.weweShop.controller;

import com.google.zxing.WriterException;
import com.wewe.weweShop.model.Order;
import com.wewe.weweShop.repository.OrderRepository;
import com.wewe.weweShop.service.CartService;
import com.wewe.weweShop.service.CurrencyService;
import com.wewe.weweShop.service.OrderService;
import com.wewe.weweShop.service.QrCodeService;
import com.wewe.weweShop.util.NumberToWords;
import com.wewe.weweShop.util.PromptPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.thymeleaf.util.NumberUtils.formatCurrency;

@Controller
@RequestMapping("/orders")
public class OrderController {

    //// สมมุติว่าเบอร์ที่ผูกกับ PromptPay คือเบอร์กลางของร้าน
    private static final String PROMPTPAY_PHONE = "0812345678";

    private final CurrencyService currencyService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService; // เพิ่ม CartService

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private PromptPayUtil promptPayUtil;

    @Autowired
    private QrCodeService qrCodeService;

    @Autowired
    public OrderController(CurrencyService currencyService, OrderService orderService, MessageSource messageSource) {
        this.currencyService = currencyService;
        this.orderService = orderService;
        this.messageSource = messageSource;
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
            orders = orderService.getOrdersByEmail(authentication); // เฉพาะของตัวเองถ้าเป็น user
        }

        // เพิ่มการแปลงสถานะเป็นข้อความภาษาไทย
        Map<Long, String> statusMap = new HashMap<>();
        Locale locale = Locale.getDefault(); // หรือ LocaleContextHolder.getLocale()

        for (Order order : orders) {
            if (order.getId() != null && order.getStatus() != null) {
                String localizedStatus = messageSource.getMessage(
                        "order.status." + order.getStatus().name(), null, locale);
                statusMap.put(order.getId(), localizedStatus);
            }
        }

        model.addAttribute("orders", orders);
        model.addAttribute("statusMap", statusMap);
        model.addAttribute("isAdmin", isAdmin);
        return "orders/list";  // ไปยัง orders.html
    }

//    @GetMapping("/my-orders")
//    public String viewPendingOrders(Model model, @AuthenticationPrincipal UserDetails userDetails) {
//        String username = userDetails.getUsername();
//
//        // ดึงออเดอร์ทั้งหมดของผู้ใช้นั้น
//        List<Order> allOrders = orderService.findByUser(username);
//
//        // กรองเฉพาะรายการที่ยังไม่ได้ชำระเงิน (PENDING_PAYMENT)
//        List<Order> pendingOrders = allOrders.stream()
//                .filter(order -> Order.Status.PENDING_PAYMENT.equals(order.getStatus()))
//                .collect(Collectors.toList());  // ✅ ใช้ได้กับ JDK 8+
//
//        // เตรียม Map สำหรับแปลงสถานะเป็นข้อความภาษาไทย
//        Map<Long, String> statusMap = new HashMap<>();
//        Locale locale = Locale.getDefault(); // หรือ LocaleContextHolder.getLocale()
//
//        for (Order order : pendingOrders) {
//            if (order.getId() != null && order.getStatus() != null) {
//                String localizedStatus = messageSource.getMessage(
//                        "order.status." + order.getStatus().name(), null, locale);
//                statusMap.put(order.getId(), localizedStatus);
//            } else {
//                System.err.println("พบ Order ที่ id หรือ status เป็น null: " + order);
//            }
//        }
//
//        model.addAttribute("orders", pendingOrders);
//        model.addAttribute("statusMap", statusMap);
//        return "my-orders"; // ชื่อของไฟล์ .html
//    }

    // OrderController.java

//    @PostMapping("/payment/confirm")
//    public String confirmPayment(@RequestParam("orderId") Long orderId, RedirectAttributes redirectAttributes) {
//        Optional<Order> optionalOrder = orderRepository.findById(orderId);
//
//        if (optionalOrder.isPresent()) {
//            Order order = optionalOrder.get();
//            // อัปเดตสถานะคำสั่งซื้อ
//            order.setStatus(Order.Status.PENDING_VERIFICATION); // กำลังตรวจสอบ
//            orderRepository.save(order);
//
//            redirectAttributes.addFlashAttribute("successMessage", "ระบบได้รับข้อมูลการชำระเงินของคุณแล้ว กรุณารอการตรวจสอบ");
//            return "redirect:/orders/" + orderId;
//        } else {
//            redirectAttributes.addFlashAttribute("errorMessage", "ไม่พบคำสั่งซื้อที่ระบุ");
//            return "redirect:/orders";
//        }
//    }

//    @GetMapping("/payment/continue")
//    public String showContinuePaymentPage(@RequestParam("orderId") Long orderId, Model model) {
//        Optional<Order> optionalOrder = orderRepository.findById(orderId);
//        if (optionalOrder.isPresent()) {
//            Order order = optionalOrder.get();
//
//            model.addAttribute("orderId", order.getId()); // ✅ ส่ง orderId
//            model.addAttribute("amount", order.getTotalAmount()); // ✅ ส่ง amount
//
//            // กรณีมี QR Code ให้สร้างและใส่
//            String qrCodeBase64 = qrCodeService.generatePromptPayQrBase64("0123456789", order.getTotalAmount());
//            model.addAttribute("qrCodeBase64", qrCodeBase64); // ✅ ส่ง QR code
//
//            return "payment/continue";
//        } else {
//            return "redirect:/orders";
//        }
//    }

//    @GetMapping("/payment/qr")
//    public String showPromptPayQr(@RequestParam("orderId") Long orderId, Model model) {
//        Optional<Order> optionalOrder = orderService.getOrderById(orderId);
//
//        if (optionalOrder.isPresent()) {
//            Order order = optionalOrder.get();
//            BigDecimal totalAmount = order.getTotalAmount();
//            String qrCodeBase64 = promptPayUtil.generateQrCodeBase64("0812345678", totalAmount);
//
//            model.addAttribute("qrCode", qrCodeBase64); // ต้องไม่เป็น null
//            model.addAttribute("amount", totalAmount);
//            model.addAttribute("orderId", orderId);
//
//            return "payment/qr";
//        } else {
//            return "redirect:/error";
//        }
//    }

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

//    @GetMapping("/success")
//    public String successPage(@RequestParam("orderId") Long orderId, Model model) {
//
//        //ดึง Order  จากฐานข้อมูล
//        model.addAttribute("orderId", orderId);
//        // ถ้าคุณต้องการดึงข้อมูลของ order ด้วย orderId
//        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalStateException("Order not found"));
//
//        // ใช้ CurrencyService แปลงจำนวนเงินให้เป็นรูปแบบที่มีคอมม่า
//        String formattedAmount = currencyService.formatCurrency(order.getTotalAmount());
//
//        // แปลงจำนวนเงินทั้งหมดเป็นคำในภาษาไทย
//        String totalAmountInWords = NumberToWords.convertToThaiCurrency(order.getTotalAmount().doubleValue());
//
//
//        model.addAttribute("order", order);
//        model.addAttribute("orderId", orderId);
//        model.addAttribute("totalAmountInWords", totalAmountInWords); // ส่งค่าไปยัง Thymeleaf
//
//        return "orders/success";  // แน่ใจว่าไฟล์ success.html อยู่ในโฟลเดอร์ resources/templates/orders
//    }

        // 🧍 สำหรับผู้ใช้ทั่วไป
    @GetMapping("/user/orders")
    public String viewUserOrders(Model model, Principal principal) {
        // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = principal.getName(); // ใช้ email หรือ username
        List<Order> orders = orderService.getOrdersByEmail(principal);
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

    @GetMapping("/list")
    public String viewOrderList(Model model, Principal principal) {
        // ตรวจสอบการล็อกอินของผู้ใช้
        if (principal == null) {
            return "redirect:/login"; // ถ้าผู้ใช้ไม่ได้ล็อกอิน ให้รีไดเรกต์ไปหน้า login
        }

        // ดึงรายการคำสั่งซื้อของผู้ใช้จาก service
        List<Order> orders = orderService.getOrdersByEmail(principal);

        // ✅ เพิ่มเติม: จัดรูปแบบราคา
        Map<Long, String> statusMap = new HashMap<>();
        Locale locale = LocaleContextHolder.getLocale(); // ใช้ LocaleContextHolder เพื่อรองรับ i18n
        for (Order order : orders) {
            if (order.getId() != null && order.getStatus() != null) {
                String localizedStatus = messageSource.getMessage(
                        "order.status." + order.getStatus().name(), null, locale);
                statusMap.put(order.getId(), localizedStatus);
            }
        }
        // ✅ เพิ่ม orderList เผื่อใช้ formatCurrency ถ้าต้องใช้ใน template
        List<Map<String, Object>> orderList = new ArrayList<>();

        for (Order order : orders) {
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("id", order.getId());
            orderMap.put("customerEmail", order.getCustomerEmail());
            orderMap.put("orderDate", order.getOrderDate());
            // ✅ เพิ่ม formatCurrency
            String formattedAmount = currencyService.formatCurrency(order.getTotalAmount());
            orderMap.put("formattedAmount", formattedAmount);
            orderList.add(orderMap);
        }

        // ส่งข้อมูลคำสั่งซื้อไปที่หน้า view
        model.addAttribute("orders", orders);
        model.addAttribute("statusMap", statusMap); // ✅ ใส่ statusMap ลง model

        return "orders/list"; // ส่งไปยังหน้ารายการคำสั่งซื้อ
    }

//    @GetMapping("/continue")
//    public String continuePayment(@RequestParam("orderId") Long orderId, Model model) {
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
//
//        if (order.getStatus() != Order.Status.PENDING_PAYMENT) {
//            return "redirect:/orders/list"; // ป้องกันการเข้า URL โดยตรงหากสถานะไม่ใช่ Pending
//        }
//
//        model.addAttribute("order", order);
//        // ไปยังหน้าชำระเงินต่อ เช่น ฟอร์มโอน, QR PromptPay หรืออื่นๆ
//        return "payment/continue";
//    }

//    @GetMapping("/payment/promptpay")
//    public String showPromptPay(@RequestParam("orderId") Long orderId, Model model) throws IOException, WriterException {
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new IllegalArgumentException("ไม่พบคำสั่งซื้อ"));
//
//        if (order.getStatus() != Order.Status.PENDING_PAYMENT) {
//            return "redirect:/orders/list";
//        }
//
//        // สมมุติหมายเลขโทรศัพท์ที่ใช้ PromptPay (คุณอาจใช้จาก config/database)
//        String phoneNumber = "0812345678";
//
//        // จำนวนเงิน
//        BigDecimal amount = order.getTotalAmount();
//
//        // สร้าง payload QR PromptPay
//        String payload = PromptPayUtil.generatePromptPayPayload(phoneNumber, amount);
//
//        // แปลง payload เป็น QR Image (Base64)
//        String qrCodeBase64 = PromptPayUtil.generateQRCodeBase64(payload);
//
//        model.addAttribute("order", order);
//        model.addAttribute("qrCodeBase64", qrCodeBase64);
//        model.addAttribute("payload", payload);
//
//        return "orders/promptpay";
//    }

//    @GetMapping("/qr/{orderId}.png")
//    public ResponseEntity<byte[]> getQrCode(@PathVariable Long orderId) {
//        Optional<Order> optionalOrder = orderService.getOrderById(orderId);
//        if (optionalOrder.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        Order order = optionalOrder.get();
//        String qrPayload = PromptPayUtil.generatePayload(PROMPTPAY_PHONE, order.getTotalAmount()); // ใส่เบอร์ผู้รับเงิน
//
//        byte[] image = promptPayUtil.generateQrCodeImage(qrPayload, 300, 300);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_PNG);
//        return new ResponseEntity<>(image, headers, HttpStatus.OK);
//    }


}
