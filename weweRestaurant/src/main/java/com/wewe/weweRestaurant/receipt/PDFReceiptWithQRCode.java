package com.wewe.weweRestaurant.receipt;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfWriter;
import org.hibernate.mapping.Map;


import java.io.FileOutputStream;

public class PDFReceiptWithQRCode {

    public static void main(String[] args) throws Exception {
        // สร้างเอกสาร PDF
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("receipt_with_qr.pdf"));

        // เปิดเอกสาร
        document.open();

        // สร้าง URL สำหรับใบเสร็จออนไลน์
        String orderId = "1004";
        String receiptUrl = "https://yourdomain.com/receipt/" + orderId;

        // สร้าง QR Code สำหรับใบเสร็จออนไลน์
        BarcodeQRCode barcodeQRCode = new BarcodeQRCode(receiptUrl,50,100, null);
        Image qrCodeImage = barcodeQRCode.getImage();
        qrCodeImage.scaleToFit(150, 150);

        // แทรกข้อความและ QR Code ลงใน PDF
        document.add(new Paragraph("ใบเสร็จการชำระเงิน"));
        document.add(new Paragraph("สแกน QR Code เพื่อดูรายละเอียดการชำระเงินออนไลน์:"));
        document.add(qrCodeImage);

        // ปิดเอกสาร
        document.close();

        System.out.println("ใบเสร็จ PDF ถูกสร้างสำเร็จ!");
    }
}

