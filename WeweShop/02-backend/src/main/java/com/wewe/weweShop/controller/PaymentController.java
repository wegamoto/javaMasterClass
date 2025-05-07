package com.wewe.weweShop.controller;

import com.wewe.weweShop.model.Order;
import com.wewe.weweShop.service.OrderService;
import com.wewe.weweShop.service.QrCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Optional;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private final OrderService orderService;
    private final QrCodeService qrCodeService;

    @Autowired
    public PaymentController(OrderService orderService, QrCodeService qrCodeService) {
        this.orderService = orderService;
        this.qrCodeService = qrCodeService;
    }

    @GetMapping("/continue")
    public String continuePayment(@RequestParam("orderId") Long orderId, Model model) {
        Optional<Order> optionalOrder = orderService.getOrderById(orderId);
        if (optionalOrder.isEmpty()) {
            return "redirect:/orders";
        }

        Order order = optionalOrder.get();

        BigDecimal amount = order.getTotalAmount();
        String phone = "0812345678"; // หรือเบอร์ promptpay ที่คุณต้องการ
        String qrCodeBase64 = qrCodeService.generatePromptPayQrBase64(phone, amount);

        model.addAttribute("orderId", orderId);
        model.addAttribute("amount", amount);
        model.addAttribute("qrCodeBase64", qrCodeBase64);

        return "payment/continue";
    }

    @PostMapping("/confirm")
    public String confirmPayment(@RequestParam("orderId") Long orderId,
                                 RedirectAttributes redirectAttributes) {
        Optional<Order> optionalOrder = orderService.getOrderById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            // ตรวจสอบสถานะก่อน (เช่น ต้องเป็น PENDING_PAYMENT)
            if (order.getStatus() == Order.Status.PENDING_PAYMENT) {
                // อัปเดตสถานะเป็น PAID หรือ COMPLETED ตามระบบของคุณ
                order.setStatus(Order.Status.PAID); // หรือ Order.Status.COMPLETED
                orderService.saveOrder(order); // หรือ orderRepository.save(order)
                redirectAttributes.addFlashAttribute("successMessage", "ชำระเงินสำเร็จแล้ว");
                redirectAttributes.addFlashAttribute("showReceiptButton", true);
                redirectAttributes.addFlashAttribute("receiptOrderId", order.getId());
            }

            return "redirect:/orders/list"; // กลับไปดูรายการคำสั่งซื้อ
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "ไม่พบคำสั่งซื้อ");
            return "redirect:/orders/list"; // ถ้าไม่พบ order
        }
    }
}

