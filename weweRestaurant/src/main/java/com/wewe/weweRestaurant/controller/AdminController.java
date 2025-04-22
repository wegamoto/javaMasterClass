package com.wewe.weweRestaurant.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.wewe.weweRestaurant.model.Order;
import com.wewe.weweRestaurant.model.OrderItem;
import com.wewe.weweRestaurant.repository.OrderRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import javax.imageio.ImageIO;

import static com.lowagie.text.FontFactory.getFont;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final OrderRepository orderRepository;

    @GetMapping("/orders")
    public String viewAllOrders(Model model) {
        List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        model.addAttribute("orders", orders);
        return "admin-orders";
    }

    @GetMapping("/orders")
    public String viewOrdersByDate(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        List<Order> orders;

        if (startDate != null && endDate != null) {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
            orders = orderRepository.findByCreatedAtBetween(startDateTime, endDateTime);
        } else {
            orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        }

        model.addAttribute("orders", orders);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "admin-orders";
    }

    @GetMapping("/orders")
    public String viewOrdersByDateAndStatus(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String status,
            Model model) {

        LocalDateTime start = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime end = (endDate != null) ? endDate.atTime(LocalTime.MAX) : null;

        List<Order> orders;

        if (start != null && end != null && status != null && !status.isEmpty()) {
            orders = orderRepository.findByCreatedAtBetweenAndStatus(start, end, status);
        } else if (start != null && end != null) {
            orders = orderRepository.findByCreatedAtBetween(start, end);
        } else if (status != null && !status.isEmpty()) {
            orders = orderRepository.findByStatus(status);
        } else {
            orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        }

        model.addAttribute("orders", orders);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("status", status);
        return "admin-orders";
    }

    @GetMapping("/orders/export/excel")
    public void exportOrdersToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=orders.xlsx");

        List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Orders");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("วันที่");
        header.createCell(1).setCellValue("โต๊ะ");
        header.createCell(2).setCellValue("สถานะ");
        header.createCell(3).setCellValue("รายการ");

        int rowIdx = 1;
        for (Order order : orders) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(order.getCreatedAt().toString());
            row.createCell(1).setCellValue(order.getTableNumber());
            row.createCell(2).setCellValue(order.getStatus());

            StringBuilder itemList = new StringBuilder();
            for (OrderItem item : order.getItems()) {
                itemList.append(item.getMenuItem().getName())
                        .append(" x ")
                        .append(item.getQuantity())
                        .append(", ");
            }
            row.createCell(3).setCellValue(itemList.toString());
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @GetMapping("/orders/export/pdf")
    public void exportOrdersToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=orders.pdf");

        List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));

        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        com.lowagie.text.Font fontHeader = getFont(FontFactory.HELVETICA_BOLD, String.valueOf(14));
        Font fontCell = getFont(FontFactory.HELVETICA);

        Paragraph title = new Paragraph("รายงานรายการออเดอร์", fontHeader);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{2, 1, 2, 5});

        // Header
        Stream.of("วันที่", "โต๊ะ", "สถานะ", "รายการอาหาร").forEach(headerTitle -> {
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(Color.LIGHT_GRAY);
            header.setPadding(5);
            header.setPhrase(new Phrase(headerTitle, fontHeader));
            table.addCell(header);
        });

        // Data
        for (Order order : orders) {
            table.addCell(new Phrase(order.getCreatedAt().toString(), fontCell));
            table.addCell(new Phrase(String.valueOf(order.getTableNumber()), fontCell));
            table.addCell(new Phrase(order.getStatus(), fontCell));

            String items = order.getItems().stream()
                    .map(item -> item.getMenuItem().getName() + " x " + item.getQuantity())
                    .collect(Collectors.joining(", "));

            table.addCell(new Phrase(items, fontCell));
        }

        document.add(table);
        document.close();
    }

    @GetMapping("/orders/{orderId}/receipt")
    public void printReceipt(@PathVariable Long orderId, HttpServletResponse response) throws IOException, WriterException {
        Order order = orderRepository.findById(orderId).orElseThrow();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=receipt_order_" + orderId + ".pdf");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        Paragraph title = new Paragraph("ใบเสร็จรับเงิน", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("เลขที่ออเดอร์: " + order.getId(), normalFont));
        document.add(new Paragraph("วันที่: " + order.getCreatedAt(), normalFont));
        document.add(new Paragraph("โต๊ะ: " + order.getTableNumber(), normalFont));
        document.add(new Paragraph("สถานะ: " + order.getStatus(), normalFont));
        document.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{4, 2, 2, 2});
        table.addCell(new Phrase("รายการ", normalFont));
        table.addCell(new Phrase("ราคา/หน่วย", normalFont));
        table.addCell(new Phrase("จำนวน", normalFont));
        table.addCell(new Phrase("ราคารวม", normalFont));

        double total = 0.0;
        for (OrderItem item : order.getItems()) {
            String name = item.getMenuItem().getName();
            double price = item.getMenuItem().getPrice();
            int qty = item.getQuantity();
            double subTotal = price * qty;
            total += subTotal;

            table.addCell(new Phrase(name, normalFont));
            table.addCell(new Phrase(String.format("%.2f", price), normalFont));
            table.addCell(new Phrase(String.valueOf(qty), normalFont));
            table.addCell(new Phrase(String.format("%.2f", subTotal), normalFont));
        }

        document.add(table);
        document.add(Chunk.NEWLINE);

        Paragraph totalPara = new Paragraph("รวมทั้งหมด: " + String.format("%.2f บาท", total), titleFont);
        totalPara.setAlignment(Element.ALIGN_RIGHT);
        document.add(totalPara);

        document.close();

        // 1. สร้าง QR Code จาก PromptPay (หรือข้อความอื่น)
        String qrText = "00020101021129370016A00000067701011101130066012345678950208TH53037646304"; // ตัวอย่าง PromptPay QR
        int size = 200;

        BitMatrix bitMatrix = new MultiFormatWriter().encode(qrText, BarcodeFormat.QR_CODE, size, size);
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

// 2. แปลง BufferedImage -> iText Image
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "png", baos);
        Image qrItext = Image.getInstance(baos.toByteArray());
        qrItext.scaleToFit(150, 150);

// 3. เพิ่มข้อความประกอบ (เช่น "สแกนชำระเงิน") และเพิ่ม QR ไปใน PDF
        document.add(Chunk.NEWLINE);
        Paragraph qrLabel = new Paragraph("สแกนชำระเงินผ่าน QR Code", normalFont);
        qrLabel.setAlignment(Element.ALIGN_CENTER);
        document.add(qrLabel);

        Paragraph qrBox = new Paragraph();
        qrBox.setAlignment(Element.ALIGN_CENTER);
        qrBox.add(qrItext);
        document.add(qrBox);

    }

}

