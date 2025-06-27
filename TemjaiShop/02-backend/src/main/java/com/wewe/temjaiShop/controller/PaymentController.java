package com.wewe.temjaiShop.controller;

import com.wewe.temjaiShop.model.Order;
import com.wewe.temjaiShop.service.OrderService;
import com.wewe.temjaiShop.service.QrCodeService;
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

    //// สมมุติว่าเบอร์ที่ผูกกับ PromptPay คือเบอร์กลางของร้าน
    private static final String PROMPTPAY_PHONE = "0909275576";

    // เปลี่ยนหมายเลขโทรศัพท์ให้เป็นหมายเลขบัญชีธนาคาร
    private static final String PROMPTPAY_ACCOUNT = "1638372343"; // หมายเลขบัญชี ธนาคาร [เต็มใจ เอ็นจิเนียริ่ง จำกัด] ยังไม่ได้ผูกพร้อมเพย์

    // หมายเลขโทรศัพท์หรือหมายเลขบัญชีธนาคาร (ที่ผูกกับพร้อมเพย์แล้ว)
    private static final String ACC_NUM_OR_PHONE = "0909275576";

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

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            BigDecimal amount = order.getTotalAmount();
            //String phone = "0909275576"; // หรือเบอร์ promptpay ที่คุณต้องการ
            String qrCodeBase64 = qrCodeService.generatePromptPayQrBase64(ACC_NUM_OR_PHONE, amount);

            model.addAttribute("orderId", orderId);
            model.addAttribute("amount", amount);
            model.addAttribute("qrCodeBase64", qrCodeBase64);

            model.addAttribute("order", order);
            // ส่งไปที่หน้า payment/continue.html ใน templates
            return "payment/continue";
        } else {
            model.addAttribute("errorMessage", "ไม่พบคำสั่งซื้อที่ระบุ");
            return "redirect:/orders/list";
        }
    }

//    @GetMapping("/continue")
//    public String continuePayment(@RequestParam("orderId") Long orderId, Model model) {
//        Optional<Order> optionalOrder = orderService.getOrderById(orderId);
//        if (optionalOrder.isEmpty()) {
//            return "redirect:/orders";
//        }
//
//        Order order = optionalOrder.get();
//
//        BigDecimal amount = order.getTotalAmount();
//        String phone = "0812345678"; // หรือเบอร์ promptpay ที่คุณต้องการ
//        String qrCodeBase64 = qrCodeService.generatePromptPayQrBase64(phone, amount);
//
//        model.addAttribute("orderId", orderId);
//        model.addAttribute("amount", amount);
//        model.addAttribute("qrCodeBase64", qrCodeBase64);
//
//        return "payment/continue";
//    }

    @PostMapping("/confirm")
    public String confirmPayment(@RequestParam("orderId") Long orderId,
                                 RedirectAttributes redirectAttributes) {
        return orderService.getOrderById(orderId)
                .map(order -> {
                    if (order.getStatus() == Order.Status.PENDING_PAYMENT) {
                        order.setStatus(Order.Status.PAID); // หรือ Order.Status.COMPLETED ตามต้องการ
                        orderService.saveOrder(order);

                        redirectAttributes.addFlashAttribute("successMessage", "ชำระเงินสำเร็จแล้ว");
                        redirectAttributes.addFlashAttribute("showReceiptButton", true);
                        redirectAttributes.addFlashAttribute("receiptOrderId", order.getId());
                    }
                    return "redirect:/orders/list";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "ไม่พบคำสั่งซื้อ");
                    return "redirect:/orders/list";
                });
    }

}

