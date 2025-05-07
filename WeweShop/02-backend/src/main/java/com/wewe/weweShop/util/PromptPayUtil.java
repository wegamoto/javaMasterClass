package com.wewe.weweShop.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Base64;
import java.util.Locale;

@Component
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

    public static String generatePromptPayPayload(String phoneNumber, BigDecimal amount) {
        String formattedPhone = formatPhoneNumber(phoneNumber);
        String payload =
                "000201" +                             // Payload Format Indicator
                        "010212" +                             // Point of Initiation Method (static:11, dynamic:12)
                        "2937" +                               // Length of Merchant Account Info
                        "0016A000000677010111" +           // Application ID (PromptPay)
                        "01130066" + formattedPhone +      // 66 = Thailand, ตามด้วยเบอร์ไม่ใส่ 0
                        "52040000" +                           // Merchant Category Code (default)
                        "5303764" +                            // Currency (764 = THB)
                        formatAmount(amount) +                 // Transaction Amount (Optional)
                        "5802TH" +                             // Country Code
                        "6304";                                // CRC (จะเติมด้านล่าง)

        String crc = calculateCRC(payload);
        return payload + crc;
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

    public static String generateQRCodeBase64(String payload) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(payload, BarcodeFormat.QR_CODE, 300, 300);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();

        return Base64.getEncoder().encodeToString(pngData);
    }

    // ฟังก์ชันสร้าง QR Code และแปลงเป็น Base64
    public String generateQrCodeBase64(String phoneNumber, BigDecimal amount) {
        try {
            // สร้างข้อมูล Payload สำหรับ PromptPay
            String payload = generatePromptPayPayload(phoneNumber, amount);

            // ใช้ QRCodeWriter สำหรับสร้าง QR Code
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(payload, BarcodeFormat.QR_CODE, 300, 300);

            // แปลง BitMatrix เป็น BufferedImage
            BufferedImage qrImage = toBufferedImage(bitMatrix);

            // แปลง BufferedImage เป็น Base64
            return convertImageToBase64(qrImage);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ฟังก์ชันแปลง BufferedImage เป็น Base64
    private String convertImageToBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        baos.flush();
        byte[] imageBytes = baos.toByteArray();
        baos.close();
        return Base64.getEncoder().encodeToString(imageBytes);
    }
//
//    // ฟังก์ชันสำหรับสร้าง Payload สำหรับ PromptPay
//    private String generatePromptPayPayload(String phoneNumber, BigDecimal amount) {
//        String amountString = String.format("%.2f", amount); // แปลงจำนวนเงินให้เป็น string 2 ตำแหน่งทศนิยม
//        String payload = "00020101021229370016A000000677010112180200" + phoneNumber + amountString + "6304";
//
//        return payload;
//    }

    // ฟังก์ชันแปลง BitMatrix เป็น BufferedImage
    private BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? 0x000000 : 0xFFFFFF); // Black or white pixel
            }
        }

        return image;
    }

    public byte[] generateQrCodeImage(String payload, int width, int height) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(payload, BarcodeFormat.QR_CODE, width, height);
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            return pngOutputStream.toByteArray();
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Error generating QR Code", e);
        }
    }
}
