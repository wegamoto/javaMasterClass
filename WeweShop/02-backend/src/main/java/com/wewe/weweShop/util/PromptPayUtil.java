package com.wewe.weweShop.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;

public class PromptPayUtil {

    public static String generatePayload(String phoneNumber, BigDecimal amount) {
        String formattedPhone = formatPhoneNumber(phoneNumber);
        String formattedAmount = formatAmount(amount);
        return buildPayload(formattedPhone, formattedAmount);
    }

    public static BufferedImage generatePromptPayQRCode(String payload, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(payload, BarcodeFormat.QR_CODE, width, height);

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(Color.BLACK);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (bitMatrix.get(x, y)) {
                        graphics.fillRect(x, y, 1, 1);
                    }
                }
            }

            return image;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String formatPhoneNumber(String phoneNumber) {
        // ลบ non-digit แล้วเพิ่มรหัสประเทศ (เช่น +66)
        phoneNumber = phoneNumber.replaceAll("\\D", "");
        if (phoneNumber.startsWith("0")) {
            phoneNumber = "66" + phoneNumber.substring(1);
        }
        return phoneNumber;
    }

    private static String formatAmount(BigDecimal amount) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(amount);
    }

    private static String buildPayload(String phone, String amount) {
        // สร้าง Payload ตามมาตรฐาน EMVCo และ PromptPay
        StringBuilder payload = new StringBuilder();
        payload.append("000201"); // Version
        payload.append("010211"); // Point of Initiation Method (11 = dynamic QR)
        payload.append("2937");   // Merchant Account Info (PromptPay specific)
        payload.append("0016A000000677010111"); // Application ID
        payload.append("01130066").append(phone); // Mobile Number

        payload.append("5303764"); // Currency (764 = THB)
        payload.append("5406").append(amount); // Amount (format: 0.00)

        String crc = calculateCRC(payload.toString() + "6304");
        payload.append("6304").append(crc); // CRC

        return payload.toString();
    }

    private static String calculateCRC(String input) {
        int crc = 0xFFFF;
        for (int i = 0; i < input.length(); i++) {
            crc ^= input.charAt(i) << 8;
            for (int j = 0; j < 8; j++) {
                crc = ((crc & 0x8000) != 0) ? (crc << 1) ^ 0x1021 : crc << 1;
            }
        }
        crc &= 0xFFFF;
        return String.format("%04X", crc).toUpperCase(Locale.ROOT);
    }
}
