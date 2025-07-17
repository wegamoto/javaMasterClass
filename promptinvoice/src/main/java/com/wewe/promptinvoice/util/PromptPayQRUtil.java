package com.wewe.promptinvoice.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.wewe.promptinvoice.model.Invoice;
import com.wewe.promptinvoice.service.InvoiceService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Optional;

public class PromptPayQRUtil {

    private final InvoiceService invoiceService;

    public PromptPayQRUtil(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    public static String generatePromptPayQR(String recipient, BigDecimal amount) {
        StringBuilder payload = new StringBuilder();

        payload.append("000201"); // Payload Format Indicator
        payload.append("010211"); // Point of Initiation Method (11 = static, 12 = dynamic)

        // Merchant Account Info
        String merchantAccount = buildMerchantAccount(recipient);
        payload.append(String.format("29%02d%s", merchantAccount.length(), merchantAccount));

        payload.append("52040000"); // Merchant Category Code (default)
        payload.append("5303764");  // Currency: 764 = THB

        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            String amountStr = amount.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
            payload.append(String.format("54%02d%s", amountStr.length(), amountStr));
        }

        payload.append("5802TH"); // Country Code
        payload.append("5908Merchant"); // Merchant Name (default)
        payload.append("6009Bangkok");  // Merchant City (default)
        payload.append("6304"); // CRC placeholder

        String crc = calculateCRC(payload.toString());
        payload.append(crc);

        return payload.toString();
    }

    private static String buildMerchantAccount(String recipient) {
        StringBuilder sb = new StringBuilder();
        sb.append("0016A000000677010111"); // PromptPay AID

        if (recipient.length() == 10) {
            // Phone number (replace leading 0 with 66)
            String mobile = recipient.startsWith("0") ? "66" + recipient.substring(1) : recipient;
            sb.append("0113").append(mobile); // 01 = mobile, 13 = length
        } else if (recipient.length() == 13) {
            // National ID
            sb.append("0213").append(recipient); // 02 = ID, 13 = length
        } else if (recipient.length() == 15) {
            // E-Wallet ID
            sb.append("0315").append(recipient); // 03 = e-wallet
        } else {
            throw new IllegalArgumentException("Invalid PromptPay ID");
        }

        return sb.toString();
    }

    public BufferedImage generateQRImage(String content, int width, int height) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    public static String getCRC16(String input) {
        int crc = 0xFFFF;
        for (char c : input.toCharArray()) {
            crc ^= (c & 0xFF) << 8;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ 0x1021;
                } else {
                    crc <<= 1;
                }
                crc &= 0xFFFF;
            }
        }
        return String.format("%04X", crc);
    }

    // CRC-16/CCITT-FALSE
    public static String calculateCRC(String input) {
        int crc = 0xFFFF;

        for (int i = 0; i < input.length(); i++) {
            crc ^= (input.charAt(i) & 0xFF) << 8;

            for (int j = 0; j < 8; j++) {
                crc = (crc & 0x8000) != 0 ? (crc << 1) ^ 0x1021 : crc << 1;
                crc &= 0xFFFF;
            }
        }

        return String.format("%04X", crc);
    }

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

        String qrContent = PromptPayQRUtil.generatePromptPayQR("0812345678", amount);

        BufferedImage image = generateQRImage(qrContent, 300, 300);

        response.setContentType("image/png");

        try (OutputStream os = response.getOutputStream()) {
            ImageIO.write(image, "png", os);
            os.flush();
        }
    }

}
