package com.wewe.weweRestaurant.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ReceiptQRCodeGenerator {

    public static void main(String[] args) throws Exception {
        // หมายเลขออเดอร์
        String orderId = "1004";  // หมายเลขออเดอร์ที่ต้องการ
        // สร้าง URL สำหรับใบเสร็จออนไลน์
        String receiptUrl = "https://yourdomain.com/receipt/" + orderId;

        // สร้าง QR Code จาก URL
        BufferedImage qrImage = generateQRCodeImage(receiptUrl);

        // บันทึก QR Code เป็นไฟล์ PNG
        ImageIO.write(qrImage, "PNG", new File("receipt_qr.png"));

        System.out.println("QR Code for receipt URL created successfully!");
    }

    // สร้าง QR Code จาก URL
    private static BufferedImage generateQRCodeImage(String data) throws Exception {
        BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 200, 200);
        return MatrixToImageWriter.toBufferedImage(matrix);
    }
}

