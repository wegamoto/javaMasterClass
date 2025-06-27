package com.wewe.temjaiShop.util;

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
public class PromptPayUtils {

    public static String generatePayload(String accountNumberOrPhone, BigDecimal amount) {
        if (accountNumberOrPhone == null || accountNumberOrPhone.isEmpty()) {
            throw new IllegalArgumentException("receiver is required");
        }
        String formattedAccount = formatAccountNumber(accountNumberOrPhone);
        String formattedAmount = formatAmount(amount);
        return buildPayload(formattedAccount, formattedAmount);
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

    public static String generatePromptPayPayload(String receiver, BigDecimal amount) {
        if (receiver == null || receiver.isEmpty()) {
            throw new IllegalArgumentException("receiver is required");
        }

        String target;
        String subTag;

        // ตรวจสอบประเภท
        if (receiver.matches("^0[0-9]{9}$")) {
            // เบอร์โทร เช่น 0812345678
            target = "66" + receiver.substring(1); // แปลงเป็น 66812345678
            subTag = "01";
        } else if (receiver.matches("^\\d{13}$")) {
            // บัตรประชาชน 13 หลัก
            target = receiver;
            subTag = "02";
        } else if (receiver.matches("^\\d{10,12}$")) {
            // บัญชีธนาคาร 10-12 หลัก
            target = receiver;
            subTag = "04";
        } else {
            throw new IllegalArgumentException("รูปแบบ receiver ไม่ถูกต้อง: ต้องเป็นเบอร์โทร, เลขบัญชี, หรือเลขบัตรประชาชน");
        }

        // เริ่มสร้าง Payload
        StringBuilder payload = new StringBuilder();
        payload.append("000201"); // Payload Format Indicator
        payload.append("010211"); // Point of Initiation Method = Static

        // Merchant Account Information - PromptPay
        String guid = "A000000677010111";
        StringBuilder merchantAccountInfo = new StringBuilder();
        merchantAccountInfo.append(String.format("00%02d%s", guid.length(), guid));
        merchantAccountInfo.append(String.format("%s%02d%s", subTag, target.length(), target));
        String merchantAccountStr = merchantAccountInfo.toString();

        payload.append(String.format("29%02d%s", merchantAccountStr.length(), merchantAccountStr));

        // Transaction Amount (optional)
        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            String amountStr = String.format("%.2f", amount);
            payload.append(String.format("54%02d%s", amountStr.length(), amountStr));
        }

        payload.append("5802TH"); // Country Code
        payload.append("6304");   // CRC16 Placeholder

        // คำนวณ CRC16-CCITT (XModem)
        String payloadStr = payload.toString();
        String crc = calculateCRC16(payloadStr);

        return payloadStr + crc;
    }

    // CRC16-CCITT (XModem): poly 0x1021, init 0xFFFF
    private static String calculateCRC16(String input) {
        byte[] bytes = input.getBytes();
        int crc = 0xFFFF;

        for (byte b : bytes) {
            crc ^= (b & 0xFF) << 8;
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

//    public static String generatePromptPayPayload(String accountNumberOrPhone, BigDecimal amount) {
//        String formattedAccount = formatAccountNumber(accountNumberOrPhone);
//        String payload =
//                "000201" +                             // Payload Format Indicator
//                        "010212" +                             // Point of Initiation Method (static:11, dynamic:12)
//                        "2937" +                               // Length of Merchant Account Info
//                        "0016A000000677010111" +           // Application ID (PromptPay)
//                        "01130066" + formattedAccount +      // 66 = Thailand, ตามด้วยเลขบัญชีหรือเบอร์โทร
//                        "52040000" +                           // Merchant Category Code (default)
//                        "5303764" +                            // Currency (764 = THB)
//                        formatAmount(amount) +                 // Transaction Amount (Optional)
//                        "5802TH" +                             // Country Code
//                        "6304";                                // CRC (จะเติมด้านล่าง)
//
//        String crc = calculateCRC(payload);
//        return payload + crc;
//    }

    private static String formatAccountNumber(String accountNumberOrPhone) {
        accountNumberOrPhone = accountNumberOrPhone.replaceAll("\\D", ""); // ลบ non-digit

        if (accountNumberOrPhone.startsWith("66") && accountNumberOrPhone.length() == 11) {
            return accountNumberOrPhone; // เบอร์โทรที่มีรหัสประเทศ
        } else if (accountNumberOrPhone.startsWith("0") && accountNumberOrPhone.length() == 10) {
            return "66" + accountNumberOrPhone.substring(1); // เปลี่ยน 0 เป็น 66
        } else if (accountNumberOrPhone.length() == 10) {
            return accountNumberOrPhone; // เลขบัญชี 10 หลัก
        } else {
            throw new IllegalArgumentException("Invalid PromptPay target: must be Thai mobile number or 10-digit bank account.");
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

    private static String buildPayload(String target, String amount) {
        String formattedTarget;
        String subTag;

        // ตรวจสอบว่าเป็นเบอร์โทร (เบอร์ไทยเริ่มด้วย 0 และมีความยาว 10 หลัก)
        if (target.matches("^0[0-9]{9}$")) {
            // ตัด 0 ด้านหน้า แล้วเพิ่มรหัสประเทศ 66
            formattedTarget = "66" + target.substring(1);
            subTag = "0113"; // Length of data (13 characters: "66" + 9 digits)
        }
        // ตรวจว่าเป็นเลขบัญชีธนาคาร (10 หลัก)
        else if (target.matches("^[0-9]{10}$")) {
            formattedTarget = target;
            subTag = "0110"; // Length of data (10 characters)
        } else {
            throw new IllegalArgumentException("Invalid PromptPay target: must be Thai mobile number or 10-digit bank account.");
        }

        StringBuilder payload = new StringBuilder();
        payload.append("000201"); // Version
        payload.append("010211"); // Point of Initiation Method (dynamic QR)

        // Merchant Account Info Template
        StringBuilder merchantAccountInfo = new StringBuilder();
        merchantAccountInfo.append("0016A000000677010111"); // Application ID (PromptPay)
        merchantAccountInfo.append(subTag).append(formattedTarget);

        // Append Merchant Account Info with total length
        String merchantAccountInfoStr = merchantAccountInfo.toString();
        payload.append("29").append(String.format("%02d", merchantAccountInfoStr.length()))
                .append(merchantAccountInfoStr);

        payload.append("5303764"); // Currency code (764 = THB)

        // Append amount
        if (amount != null && !amount.isEmpty()) {
            double value = Double.parseDouble(amount);
            if (value > 0) {
                payload.append("54").append(String.format("%02d", amount.length()))
                        .append(amount);
            }
        }

        // Country code
        payload.append("5802TH");

        // CRC
        String crc = calculateCRC(payload.toString() + "6304");
        payload.append("6304").append(crc);

        return payload.toString();
    }

    public static String generatePromptPayPayloadForKasikorn(String accountNumber, BigDecimal amount) {
        String formattedAccount = formatAccountNumber(accountNumber); // Format เลขบัญชี
        String payload =
                "000201" +                             // Payload Format Indicator
                        "010212" +                             // Point of Initiation Method (static:11, dynamic:12)
                        "2937" +                               // Length of Merchant Account Info
                        "0016A000000677010111" +               // Application ID (PromptPay)
                        "01130066" + formattedAccount +        // 66 = Thailand, ตามด้วยเลขบัญชี (ตัวอย่าง: 1234567890)
                        "52040000" +                           // Merchant Category Code (default)
                        "5303764" +                            // Currency (764 = THB)
                        formatAmount(amount) +                 // Transaction Amount (Optional)
                        "5802TH" +                             // Country Code
                        "6304";                                // CRC (จะเติมด้านล่าง)

        String crc = calculateCRC(payload);
        return payload + crc;
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
