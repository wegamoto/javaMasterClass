package com.wewe.temjaiShop.controller;

import com.wewe.temjaiShop.dto.ProductReportDTO;
import com.wewe.temjaiShop.model.Product;
import com.wewe.temjaiShop.model.ProductDto;
import com.wewe.temjaiShop.repository.ProductRepository;
import com.wewe.temjaiShop.service.ProductService;
import com.wewe.temjaiShop.util.NumberUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminStockController {

    private final ProductService productService;

    private final ProductRepository productRepository;

    @Autowired
    public AdminStockController(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @GetMapping("/stock-view")
    public String viewStock(Model model) {
        // โหลดข้อมูลสินค้าจากฐานข้อมูล
        List<Product> products = productRepository.findAll();

        List<ProductDto> stockProducts = products.stream()
                .map(p -> {
                    ProductDto dto = new ProductDto();
                    dto.setName(p.getName());
                    dto.setCategory(p.getCategory() != null ? p.getCategory().getName() : "ไม่ระบุ");
                    dto.setFormattedPrice(p.getPrice() != null ? NumberUtils.formatPrice(p.getPrice()) + " บาท" : "N/A");
                    dto.setStock(p.getStockQuantity());
                    return dto;
                })
                .toList();

        int totalStock = products.stream().mapToInt(Product::getStockQuantity).sum(); // รวม stock

        model.addAttribute("stockProducts", products);
        model.addAttribute("totalStock", totalStock); // ส่งค่าไปที่ View

        return "admin/stock-view"; // ต้องมีไฟล์ชื่อ stock-view.html อยู่ใน templates/admin/
    }

    @GetMapping("/stock-add")
    public String showAddStockForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/stock-add";
    }

//    @GetMapping("/stock-edit")
//    public String editStock(@RequestParam("id") Long id, Model model) {
//        Optional<Product> product = productRepository.findById(id);
//        model.addAttribute("product", product);
//        return "admin/stock-edit"; // ✅ ต้องตรงกับ path ใน templates/
//    }

    @GetMapping("/stock-edit/{id}")
    public String editStockForm(@PathVariable("id") Long id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + id));
        model.addAttribute("product", product);
        return "admin/stock-edit";
    }

    @Transactional
    @PostMapping("/stock-edit/{id}")
    public String updateStock(@PathVariable("id") Long id,
                              @ModelAttribute Product updatedProduct,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              Model model) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + id));

        // ✅ อัปเดตข้อมูลทั่วไป
        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setPrice(updatedProduct.getPrice());
        product.setStockQuantity(updatedProduct.getStockQuantity());

        // ✅ อัปโหลดรูปภาพใหม่ (ถ้ามี)
        if (!imageFile.isEmpty()) {
            String contentType = imageFile.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                model.addAttribute("error", "เฉพาะไฟล์ภาพเท่านั้นที่อนุญาต");
                model.addAttribute("product", product);
                return "admin/stock-edit";
            }

            try {
                String fileName = UUID.randomUUID() + "-" + imageFile.getOriginalFilename();
                Path uploadPath = Paths.get("uploads/");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                product.setImage("/uploads/" + fileName); // ✅ พาธที่ frontend ใช้
            } catch (IOException e) {
                model.addAttribute("error", "การอัปโหลดไฟล์ล้มเหลว: " + e.getMessage());
                model.addAttribute("product", product);
                return "admin/stock-edit";
            }
        } else {
            // ✅ ถ้าไม่มีการอัปโหลดภาพ → ใช้ภาพเดิม
            product.setImage(updatedProduct.getImage());
        }

        productRepository.save(product);
        return "redirect:/admin/stock-view";
    }


    @GetMapping("/stock-delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return "redirect:/admin/stock-view";
    }

    @GetMapping("/stock-report")
    public String stockReport(Model model) {
        List<Product> products = productService.findAll();

        List<ProductReportDTO> reportList = products.stream()
                .map(ProductReportDTO::new)
                .collect(Collectors.toList());

        BigDecimal totalStockValue = reportList.stream()
                .map(ProductReportDTO::getTotalValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("reportList", reportList);
        model.addAttribute("totalValue", totalStockValue);

        return "admin/stock-report";
    }

    @GetMapping("/stock-low-alert")
    public String lowStockAlert(Model model) {
        List<Product> lowStockProducts = productService.findLowStockProducts(5); // สินค้าน้อยกว่า 5 ชิ้น
        model.addAttribute("lowStockProducts", lowStockProducts);
        return "admin/stock-low-alert";
    }

    @GetMapping("/stock-upload")
    public String showStockUploadPage() {
        return "admin/stock-upload"; // ชี้ไปที่ไฟล์ stock-upload.html
    }

    @PostMapping("/stock-upload")
    public String uploadStockExcel(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            productService.importStockFromExcel(file);
            redirectAttributes.addFlashAttribute("success", "อัปโหลดสต๊อกเรียบร้อยแล้ว");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "เกิดข้อผิดพลาด: " + e.getMessage());
        }
        return "redirect:/admin/stock-view";
    }

    @GetMapping("/admin/download-stock-template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Stock Template");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("name");
        header.createCell(1).setCellValue("category");
        header.createCell(2).setCellValue("price");
        header.createCell(3).setCellValue("stockQuantity");

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=stock-template.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

}

