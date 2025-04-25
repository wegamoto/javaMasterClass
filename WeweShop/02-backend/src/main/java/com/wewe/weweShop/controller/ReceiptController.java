package com.wewe.weweShop.controller;

import com.wewe.weweShop.service.ReceiptService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/receipt")
public class ReceiptController {

    private final ReceiptService receiptService;

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
}

