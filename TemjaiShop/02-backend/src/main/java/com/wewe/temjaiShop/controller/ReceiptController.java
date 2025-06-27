package com.wewe.temjaiShop.controller;

import com.wewe.temjaiShop.model.Order;
import com.wewe.temjaiShop.service.OrderService;
import com.wewe.temjaiShop.service.ReceiptService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/receipt")
public class ReceiptController {

    private final ReceiptService receiptService;
    private OrderService orderService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @GetMapping("/{orderId}")
    public void getReceipt(@PathVariable Long orderId,
                           @RequestParam("phone") String phoneNumber,
                           HttpServletResponse response) throws Exception {
        byte[] pdf = receiptService.generateReceiptPdf(orderId, phoneNumber);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=receipt-" + orderId + ".pdf");
        response.getOutputStream().write(pdf);
    }

    @GetMapping("/receipt")
    public String viewReceipt(@RequestParam("orderId") Long orderId, Model model) {
        Optional<Order> order = orderService.getOrderById(orderId);
        if (order.isPresent()) {
            model.addAttribute("order", order.get());
            return "orders/receipt"; // ไปยัง receipt.html
        }
        return "redirect:/orders?error=receipt-not-found";
    }
}

