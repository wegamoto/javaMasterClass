package com.wewe.weweRestaurant.service;


import com.wewe.weweRestaurant.payment.PromptPayQRCodeGenerator; // import คลาส

public class ReceiptService {

    public void generateReceiptWithQR(String phoneNumber, double amount) {
        // สร้าง QR Code พร้อมเพย์
        String qrData = PromptPayQRCodeGenerator.generatePromptPayData(phoneNumber, amount);

        // สร้าง QR Code และแสดงใน PDF
        // (รหัสที่ใช้ในการสร้าง PDF เช่น iText หรือ OpenPDF)
        generatePDFWithQR(qrData);
    }

    private void generatePDFWithQR(String qrData) {
        // Logic สำหรับการสร้างใบเสร็จ PDF พร้อม QR Code
    }
}

