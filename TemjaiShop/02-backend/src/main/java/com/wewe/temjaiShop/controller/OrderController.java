package com.wewe.temjaiShop.controller;

import com.wewe.temjaiShop.dto.OrderWithBadge;
import com.wewe.temjaiShop.model.Order;
import com.wewe.temjaiShop.model.User;
import com.wewe.temjaiShop.repository.OrderRepository;
import com.wewe.temjaiShop.repository.UserRepository;
import com.wewe.temjaiShop.service.CartService;
import com.wewe.temjaiShop.service.CurrencyService;
import com.wewe.temjaiShop.service.OrderService;
import com.wewe.temjaiShop.service.QrCodeService;
import com.wewe.temjaiShop.util.PromptPayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/orders")
public class OrderController {

    //// สมมุติว่าเบอร์ที่ผูกกับ PromptPay คือเบอร์กลางของร้าน
    //private static final String PROMPTPAY_PHONE = "0909275576";

    // เปลี่ยนหมายเลขโทรศัพท์ให้เป็นหมายเลขบัญชีธนาคาร
    // private static final String PROMPTPAY_ACCOUNT = "1638372343"; // หมายเลขบัญชี ธนาคาร [เต็มใจ เอ็นจิเนียริ่ง จำกัด] ยังไม่ได้ผูกพร้อมเพย์

    // หมายเลขโทรศัพท์หรือหมายเลขบัญชีธนาคาร (ที่ผูกกับพร้อมเพย์แล้ว)
    private static final String ACC_NUM_OR_PHONE = "0909275576";


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
    private PromptPayUtils promptPayUtil;

    @Autowired
    private QrCodeService qrCodeService;

    @Autowired
    private UserRepository userRepository;

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
            orders = orderService.findOrdersByUsername(email); // เฉพาะของตัวเองถ้าเป็น user
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

        List<OrderWithBadge> ordersWithBadge = orders.stream()
                .map(order -> new OrderWithBadge(order, getStatusBadgeClass(order.getStatus())))
                .toList();

        // model.addAttribute("orders", orders);
        model.addAttribute("orders", ordersWithBadge);
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

    @PostMapping("/payment/confirm")
    public String confirmPayment(@RequestParam("orderId") Long orderId, RedirectAttributes redirectAttributes) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            // อัปเดตสถานะคำสั่งซื้อ
            order.setStatus(Order.Status.PENDING_VERIFICATION); // กำลังตรวจสอบ
            orderRepository.save(order);

            redirectAttributes.addFlashAttribute("successMessage", "ระบบได้รับข้อมูลการชำระเงินของคุณแล้ว กรุณารอการตรวจสอบ");
            return "redirect:/orders/" + orderId;
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "ไม่พบคำสั่งซื้อที่ระบุ");
            return "redirect:/orders";
        }
    }

    @GetMapping("/payment/continue")
    public String showContinuePaymentPage(@RequestParam("orderId") Long orderId, Model model) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            model.addAttribute("orderId", order.getId()); // ✅ ส่ง orderId
            model.addAttribute("amount", order.getTotalAmount()); // ✅ ส่ง amount

            // กรณีมี QR Code ให้สร้างและใส่
            String qrCodeBase64 = qrCodeService.generatePromptPayQrBase64(ACC_NUM_OR_PHONE, order.getTotalAmount());
            model.addAttribute("qrCodeBase64", qrCodeBase64); // ✅ ส่ง QR code

            return "payment/continue";
        } else {
            return "redirect:/orders";
        }
    }

    @GetMapping("/payment/qr")
    public String showPromptPayQr(@RequestParam("orderId") Long orderId, Model model) {
        Optional<Order> optionalOrder = orderService.getOrderById(orderId);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            BigDecimal totalAmount = order.getTotalAmount();

            // String qrCodeBase64 = promptPayUtil.generateQrCodeBase64("0909275576", totalAmount);
            // ใช้บัญชีธนาคารในการสร้าง PromptPay QR Code
            String qrCodeBase64 = promptPayUtil.generateQrCodeBase64(ACC_NUM_OR_PHONE, totalAmount);


            model.addAttribute("qrCode", qrCodeBase64); // ต้องไม่เป็น null
            model.addAttribute("amount", totalAmount);
            model.addAttribute("orderId", orderId);

            return "payment/qr";
        } else {
            return "redirect:/error";
        }
    }

    @PostMapping("/checkout")
    public String checkout(Principal principal,
                           RedirectAttributes redirect,  // สำหรับ flash message
                           Model model) {

        // 1) ตรวจว่าล็อกอินแล้วหรือยัง
        if (principal == null) {
            redirect.addFlashAttribute("error", "กรุณาเข้าสู่ระบบก่อนทำรายการ");
            return "redirect:/login";
        }

        // 2) ดึงอีเมลจาก Principal (ซึ่งจะได้ username/email)
        String userEmail = principal.getName();
        if (userEmail == null) {
            redirect.addFlashAttribute("error", "ไม่สามารถระบุอีเมลผู้ใช้ได้");
            return "redirect:/login";
        }

        // 3) ดึง User จากฐานข้อมูล
        Optional<User> userOpt = userRepository.findByUsernameOrEmail(userEmail);
        if (userOpt.isEmpty()) {
            redirect.addFlashAttribute("error", "ไม่พบข้อมูลผู้ใช้ในระบบ");
            return "redirect:/login";
        }

        User user = userOpt.get();

        try {
            // 4) ทำ checkout — cartService คืน orderId
            Long orderId = cartService.checkout(user.getId());

            // 5) ส่งต่อไปหน้า success
            return "redirect:/orders/success?orderId=" + orderId;

        } catch (IllegalStateException ex) {
            // เช่น ตะกร้าว่าง
            redirect.addFlashAttribute("error", ex.getMessage());
            return "redirect:/cart/view";

        } catch (Exception ex) {
            redirect.addFlashAttribute("error", "เกิดข้อผิดพลาดขณะทำรายการ#1");
            return "redirect:/cart/view";
        }
    }

    /* ---------- helper ---------- */
    private String extractUserEmail(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails user) {                 // Local login
            return user.getUsername();                               // มักเก็บอีเมลไว้ที่ username
        }
        if (principal instanceof OAuth2User oauth) {                 // OAuth2 (Facebook / Google)
            Object email = oauth.getAttribute("email");              // provider ต้องส่ง email มาก่อน
            return email != null ? email.toString() : null;
        }
        if (principal instanceof String str) {                       // fallback เป็น String
            return str;
        }
        return null;
    }

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
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = extractEmailFromPrincipal(principal);
        List<Order> orders = orderService.getOrdersByUserEmail(userEmail);
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

        // ✅ ดึง email จาก Principal ทั้งแบบ Local และ OAuth2
        String userEmail = extractEmailFromPrincipal(principal);
        User user = userRepository.findByUsernameOrEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));

        if (userEmail == null) {
            throw new IllegalStateException("ไม่สามารถดึงอีเมลจาก principal ได้");
        }

        System.out.println("***999***Current user: " + principal.getName());

        // ดึงรายการคำสั่งซื้อของผู้ใช้จาก service
        List<Order> orders = orderRepository.findByUserId(user.getId());

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
        model.addAttribute("orderList", orderList); // ใช้ใน template ได้

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

    private String extractEmailFromPrincipal(Principal principal) {
        if (principal instanceof OAuth2AuthenticationToken oauthToken) {
            Object principalObj = oauthToken.getPrincipal();

            // ถ้า principal เป็น OidcUser (OpenID Connect) จะมี getEmail()
            if (principalObj instanceof OidcUser oidcUser) {
                String email = oidcUser.getEmail();
                if (email != null && !email.isBlank()) {
                    return email;
                }
            }

            // กรณีทั่วไปของ OAuth2 ดึงจาก attributes
            Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();
            Object emailObj = attributes.get("email");
            if (emailObj instanceof String emailStr && !emailStr.isBlank()) {
                return emailStr;
            }
            // fallback: ใช้ id ถ้าไม่มี email
            Object idObj = attributes.get("id");
            if (idObj instanceof String idStr && !idStr.isBlank()) {
                return idStr;
            }

            throw new IllegalStateException("OAuth2 user ไม่มี email หรือ id");

        } else if (principal instanceof UsernamePasswordAuthenticationToken authToken) {
            // Local login ใช้ getName() ที่มักจะเป็น username หรือ email
            return authToken.getName();
        }

        // fallback
        return principal.getName();
    }

    private String getStatusBadgeClass(Order.Status status) {
        return switch (status) {
            case CREATED -> "bg-info text-dark";
            case WAITING_FOR_PAYMENT, PENDING_PAYMENT -> "bg-warning text-dark";
            case PENDING_VERIFICATION -> "bg-secondary";
            case PAID -> "bg-primary";
            case COMPLETED -> "bg-success";
            case CANCELLED -> "bg-danger";
            default -> "bg-light text-dark";
        };
    }

}
