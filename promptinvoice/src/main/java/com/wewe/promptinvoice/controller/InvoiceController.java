package com.wewe.promptinvoice.controller;

import ch.qos.logback.core.Context;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.WriterException;
import com.wewe.promptinvoice.model.Invoice;
import com.wewe.promptinvoice.model.InvoiceStatus;
import com.wewe.promptinvoice.model.Item;
import com.wewe.promptinvoice.repository.InvoiceRepository;
import com.wewe.promptinvoice.service.BarcodeService;
import com.wewe.promptinvoice.service.InvoiceService;
import com.wewe.promptinvoice.service.PaymentService;
import com.wewe.promptinvoice.util.PromptPayQRUtil;
import com.wewe.promptinvoice.util.QRCodeUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring6.SpringTemplateEngine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceService invoiceService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
    private final BarcodeService barcodeService;
    private final PaymentService paymentService;

    private final SpringTemplateEngine templateEngine;

    @Value("classpath:/fonts/THSarabunNew.ttf")
    private Resource thaiFont;

    public InvoiceController(InvoiceRepository invoiceRepository, InvoiceService invoiceService,
                             BarcodeService barcodeService, PaymentService paymentService, SpringTemplateEngine templateEngine) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceService = invoiceService;
        this.barcodeService = barcodeService;
        this.paymentService = paymentService;
        this.templateEngine = templateEngine;
    }

    @GetMapping
    public String listInvoices(Model model, HttpServletRequest request) {
        List<Invoice> invoices = invoiceRepository.findAll();
        List<DashboardController.InvoiceViewModel> viewModels = invoices.stream()
                .map(DashboardController.InvoiceViewModel::fromInvoice)
                .toList();

        model.addAttribute("invoices", viewModels);
        model.addAttribute("requestURI", request.getRequestURI());
        return "invoice_list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model, HttpServletRequest request) {
        model.addAttribute("invoice", new Invoice());
        model.addAttribute("requestURI", request.getRequestURI());
        return "invoice-form";
    }

    @PostMapping
    public String createInvoice(@ModelAttribute Invoice invoice) {
        invoiceRepository.save(invoice);
        return "redirect:/invoices";
    }

    @GetMapping("/{id}")
    public String viewInvoice(@PathVariable Long id, Model model, HttpServletRequest request) throws IOException, WriterException {
        Optional<Invoice> invoiceOpt = invoiceService.findById(id);
        if (invoiceOpt.isEmpty()) return "error/404";

        Invoice invoice = invoiceOpt.get();
        List<Item> items = objectMapper.readValue(invoice.getItemsJson(), new TypeReference<>() {});
        List<FormattedItemViewModel> formattedItems = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (Item item : items) {
            BigDecimal price = BigDecimal.valueOf(item.getPrice());
            int quantity = item.getQuantity();
            BigDecimal lineTotal = price.multiply(BigDecimal.valueOf(quantity));
            subtotal = subtotal.add(lineTotal);

            formattedItems.add(new FormattedItemViewModel(item.getDescription(), price, quantity, decimalFormat));
        }

        BigDecimal vatAmount = invoice.isVatApplied() ? subtotal.multiply(BigDecimal.valueOf(0.07)) : BigDecimal.ZERO;
        BigDecimal totalWithVat = subtotal.add(vatAmount);

        // ✅ เพิ่ม QR Code
        String qrContent = PromptPayQRUtil.generatePromptPayQR("0812345678", totalWithVat);
        BufferedImage qrImage = QRCodeUtil.generateQRImage(qrContent, 250, 250);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "png", baos);
        String base64Qr = Base64.getEncoder().encodeToString(baos.toByteArray());

        model.addAttribute("invoice", invoice);
        model.addAttribute("items", formattedItems);
        model.addAttribute("subtotalFormatted", decimalFormat.format(subtotal));
        model.addAttribute("vatFormatted", decimalFormat.format(vatAmount));
        model.addAttribute("totalAmountFormatted", decimalFormat.format(totalWithVat));
        model.addAttribute("createdAtFormatted", formatDate(LocalDate.from(invoice.getCreatedAt())));
        model.addAttribute("requestURI", request.getRequestURI());

        // ✅ ส่ง QR ไปยัง view
        model.addAttribute("qrBase64", base64Qr);

        return "invoice_view";
    }


    // แสดงหน้ารวมรายการใบแจ้งหนี้ สำหรับแก้ไขสถานะ
    @GetMapping("/status/edit")
    public String showEditStatusPage(Model model) {
        List<Invoice> invoices = invoiceRepository.findAll();
        model.addAttribute("invoices", invoices);
        model.addAttribute("statuses", InvoiceStatus.values());
        return "invoices/edit-status";
    }

    // อัปเดตสถานะทีละรายการ
    @PostMapping("/{id}/status")
    public String updateInvoiceStatus(@PathVariable Long id, @RequestParam("status") InvoiceStatus status) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invoice ID ไม่ถูกต้อง: " + id));

        invoice.setStatus(status);
        invoiceRepository.save(invoice);

        return "redirect:/invoices/status/edit"; // หรือ path ที่เหมาะกับ view ของคุณ
    }

    private String formatDate(java.time.LocalDate date) {
        return date != null ? date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
    }

    @Getter
    public static class FormattedItemViewModel {

        private final String description;
        private final BigDecimal price;
        private final int quantity;
        private final String priceFormatted;
        private final String totalFormatted;

        public FormattedItemViewModel(String description, BigDecimal price, int quantity, DecimalFormat df) {
            this.description = description;
            this.price = price;
            this.quantity = quantity;
            this.priceFormatted = df.format(price);
            this.totalFormatted = df.format(price.multiply(BigDecimal.valueOf(quantity)));
        }
    }

    @GetMapping("/barcode/{invoiceId}")
    @ResponseBody
    public ResponseEntity<byte[]> generateInvoiceBarcode(@PathVariable Long invoiceId) throws Exception {
        String code = "INV" + invoiceId;
        byte[] image = barcodeService.generateBarcodeImage(code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }

    @PostMapping("/invoices/{id}/pay")
    public String processPayment(@PathVariable Long id,
                                 @RequestParam BigDecimal amount,
                                 @RequestParam String method) {
        paymentService.addPayment(id, amount, method);
        return "redirect:/invoices/" + id;
    }

    @GetMapping("/invoice/{id}/qr-image")
    @ResponseBody
    public String getQRBase64(@PathVariable Long id) throws Exception {
        Invoice invoice = invoiceService.findById(id).orElseThrow();
        String content = PromptPayQRUtil.generatePromptPayQR("0812345678", BigDecimal.valueOf(invoice.getTotalAmount()));

        BufferedImage qrImage = QRCodeUtil.generateQRImage(content, 300, 300);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrImage, "png", baos);
        String base64Image = Base64.getEncoder().encodeToString(baos.toByteArray());

        return "data:image/png;base64," + base64Image;
    }

//    @GetMapping("/invoice/{id}/pdf")
//    public void generateInvoicePdf(@PathVariable Long id, HttpServletResponse response) throws Exception {
//        Invoice invoice = invoiceService.findById(id).orElseThrow();
//
//        String qrContent = PromptPayQRUtil.generatePromptPayQR("0812345678", BigDecimal.valueOf(invoice.getTotalAmount()));
//        BufferedImage qrImage = QRCodeUtil.generateQRImage(qrContent, 250, 250);
//
//        // แปลง QR เป็น Base64
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ImageIO.write(qrImage, "png", baos);
//        String base64Qr = Base64.getEncoder().encodeToString(baos.toByteArray());
//
//        // เตรียม HTML
//        Context context = new Context();
//        context.setVariable("invoice", invoice);
//        context.setVariable("qrBase64", base64Qr);
//
//        String html = templateEngine.process("invoice_pdf", context);  // invoice_pdf.html ใน resources/templates/
//
//        // สร้าง PDF
//        response.setContentType("application/pdf");
//        response.setHeader("Content-Disposition", "inline; filename=invoice_" + id + ".pdf");
//
//        PdfRendererBuilder builder = new PdfRendererBuilder();
//        builder.useFastMode();
//        builder.withHtmlContent(html, "");
//        builder.toStream(response.getOutputStream());
//        builder.run();
//    }


}
