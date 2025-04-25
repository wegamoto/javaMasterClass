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

            double total = 0.0;
            for (OrderItem item : order.getItems()) {
                double subtotal = item.getPrice() * item.getQuantity();
                total += subtotal;
                table.addCell(item.getProductName());
                table.addCell(String.format("%.2f", item.getPrice()));
                table.addCell(String.valueOf(item.getQuantity()));
                table.addCell(String.format("%.2f", subtotal));
            }

            document.add(table);
            document.add(new Paragraph("Total: " + String.format("%.2f", total)));

            // Generate QR Code
            String payload = PromptPayUtil.generatePayload(phoneNumber, total);
            BufferedImage qrImage = PromptPayUtil.generatePromptPayQRCode(payload, 200, 200);
            ByteArrayOutputStream imageBaos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "PNG", imageBaos);
            Image qr = new Image(ImageDataFactory.create(imageBaos.toByteArray()));
            document.add(new Paragraph("PromptPay QR Code:"));
            document.add(qr);

            document.close();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }
}
