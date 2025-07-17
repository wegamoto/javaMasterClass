package com.wewe.promptinvoice.controller;

import com.wewe.promptinvoice.model.Invoice;
import com.wewe.promptinvoice.service.InvoiceService;
import com.wewe.promptinvoice.service.SettingService;
import com.wewe.promptinvoice.util.PromptPayQRUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Optional;

import static com.wewe.promptinvoice.util.QRCodeUtil.generateQRImage;

@RequiredArgsConstructor
@Controller
public class QRCodeController {

    private final InvoiceService invoiceService;
    private final SettingService settingService;

    @GetMapping("/invoice/{id}/qr")
    @ResponseBody
    public void generateQR(@PathVariable Long id, HttpServletResponse response) throws Exception {
        Optional<Invoice> optionalInvoice = invoiceService.findById(id);

        if (optionalInvoice.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "ไม่พบใบแจ้งหนี้");
            return;
        }

        Invoice invoice = optionalInvoice.get();
        BigDecimal amount = BigDecimal.valueOf(invoice.getTotalAmount());

        // ✅ ดึงเบอร์ PromptPay จาก settings
        String promptPayNumber = settingService.getPromptPayPhone();

        String qrContent = PromptPayQRUtil.generatePromptPayQR(promptPayNumber, amount);
        BufferedImage image = generateQRImage(qrContent, 300, 300);

        response.setContentType("image/png");

        try (OutputStream os = response.getOutputStream()) {
            ImageIO.write(image, "png", os);
            os.flush();
        }
    }
}

