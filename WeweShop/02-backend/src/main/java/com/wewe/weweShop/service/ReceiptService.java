// ReceiptService.java
package com.wewe.weweShop.service;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import com.wewe.weweShop.model.Order;
import com.wewe.weweShop.model.OrderItem;
import com.wewe.weweShop.repository.OrderRepository;
import com.wewe.weweShop.util.PromptPayUtil;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import javax.imageio.ImageIO;

@Service
public class ReceiptService {

    private final OrderRepository orderRepository;

    public ReceiptService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public byte[] generateReceiptPdf(Long orderId, String phoneNumber) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order ID"));
        return generateReceiptPdf(order, phoneNumber);
    }

    public byte[] generateReceiptPdf(Order order, String phoneNumber) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Receipt for Order #" + order.getId()));
            document.add(new Paragraph("Customer: " + order.getCustomerName()));

            Table table = new Table(UnitValue.createPercentArray(new float[]{4, 2, 2, 2}))
                    .useAllAvailableWidth();
            table.addCell("Product");
            table.addCell("Price");
            table.addCell("Quantity");
            table.addCell("Subtotal");

            BigDecimal total = BigDecimal.ZERO; // เริ่มต้นที่ 0
            for (OrderItem item : order.getItems()) {
                BigDecimal price = item.getPrice(); // ราคา
                BigDecimal quantity = BigDecimal.valueOf(item.getQuantity()); // จำนวน
                BigDecimal subtotal = price.multiply(quantity); // คำนวณ Subtotal
                total = total.add(subtotal); // บวกยอดรวม

                // แสดงข้อมูลในตาราง
                table.addCell(item.getProductName());
                table.addCell(String.format("%.2f", price)); // แสดงราคาในรูปแบบ 2 ทศนิยม
                table.addCell(String.valueOf(item.getQuantity())); // จำนวนสินค้า
                table.addCell(String.format("%.2f", subtotal)); // แสดง Subtotal ในรูปแบบ 2 ทศนิยม
            }

            document.add(table);

            // แสดงยอดรวม
            document.add(new Paragraph("Total: " + String.format("%.2f", total)));

            // สร้าง QR Code สำหรับ PromptPay
            String payload = PromptPayUtil.generatePayload(phoneNumber, total);
            BufferedImage qrImage = PromptPayUtil.generatePromptPayQRCode(payload, 200, 200);
            ByteArrayOutputStream imageBaos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "PNG", imageBaos);
            Image qr = new Image(ImageDataFactory.create(imageBaos.toByteArray()));

            // แสดง QR Code ใน PDF
            document.add(new Paragraph("PromptPay QR Code:"));
            document.add(qr);

            document.close();
            return baos.toByteArray(); // ส่งคืนเป็น byte array ของ PDF
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

}
