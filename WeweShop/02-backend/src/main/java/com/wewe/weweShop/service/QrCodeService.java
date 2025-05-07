package com.wewe.weweShop.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class QrCodeService {

    public String generatePromptPayQrBase64(String phoneOrId, BigDecimal amount) {
        String payload = generatePromptPayPayload(phoneOrId, amount);
        BufferedImage qrImage = generateQrCodeImage(payload);
        return encodeImageToBase64(qrImage);
    }

    private String generatePromptPayPayload(String receiver, BigDecimal amount) {
        // ตัด non-digit และ format เบอร์โทรให้ตรงตาม PromptPay (เช่น 0812345678 -> 66812345678)
        if (receiver.length() == 10 && receiver.startsWith("0")) {
            receiver = "66" + receiver.substring(1);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("000201"); // Payload format indicator
        sb.append("010212"); // Point of Initiation Method = dynamic (12)

        // Merchant Account Information
        sb.append("2937"); // Length of this field
        sb.append("0016A000000677010111"); // PromptPay Application ID
        sb.append("01130066").append(receiver); // Phone number (13 digits)

        // Transaction amount
        String amountStr = String.format("%.2f", amount);
        sb.append(String.format("5405%s", amountStr.replace(".", ""))); // Amount

        sb.append("5802TH"); // Country Code
        sb.append("6304"); // CRC placeholder

        // Generate CRC16 (for validation)
        String withoutCrc = sb.toString();
        String crc = calculateCRC16(withoutCrc);
        return withoutCrc + crc;
    }

    private BufferedImage generateQrCodeImage(String text) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 300, 300, hints);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException e) {
            throw new RuntimeException("Error generating QR Code", e);
        }
    }

    private String encodeImageToBase64(BufferedImage image) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            javax.imageio.ImageIO.write(image, "png", out);
            byte[] bytes = out.toByteArray();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encode QR code to base64", e);
        }
    }

    private String calculateCRC16(String input) {
        int crc = 0xFFFF;
        for (char c : input.toCharArray()) {
            crc ^= c << 8;
            for (int j = 0; j < 8; j++) {
                crc = (crc & 0x8000) != 0 ? (crc << 1) ^ 0x1021 : (crc << 1);
            }
        }
        crc &= 0xFFFF;
        return String.format("%04X", crc);
    }
}

