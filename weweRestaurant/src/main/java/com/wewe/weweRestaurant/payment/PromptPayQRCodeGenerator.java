package com.wewe.weweRestaurant.payment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PromptPayQRCodeGenerator {

    public static void main(String[] args) throws Exception {
        // ข้อมูลการชำระเงิน PromptPay
        String promptPayData = generatePromptPayData("1234567890", 100.50); // เบอร์พร้อมเพย์และจำนวนเงิน

        // สร้าง QR Code
        BufferedImage qrImage = generateQRCodeImage(promptPayData);

        // บันทึก QR Code เป็นไฟล์ หรือแสดงใน PDF
        ImageIO.write(qrImage, "PNG", new java.io.File("promptpay_qr.png"));
    }

    // สร้างข้อมูล QR Code พร้อมเพย์ (ตามมาตรฐาน EMVCo)
    public static String generatePromptPayData(String phoneNumber, double amount) {
        String currency = "TH";  // สกุลเงินบาท
        String transactionAmount = String.format("%.2f", amount);  // จำนวนเงิน
        String payload = "00020101021129370016A00000067701011101130066" +
                phoneNumber + "50208" + currency + "530376" +
                String.format("%.0f", amount * 100);  // การคำนวณจำนวนเงินเป็นเซ็นต์

        return payload;
    }

    // สร้าง QR Code จากข้อมูล
    private static BufferedImage generateQRCodeImage(String data) throws Exception {
        BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 200, 200);
        return MatrixToImageWriter.toBufferedImage(matrix);
    }
}

