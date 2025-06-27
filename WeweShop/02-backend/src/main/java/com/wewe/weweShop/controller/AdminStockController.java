package com.wewe.weweShop.controller;

import com.wewe.weweShop.model.Product;
import com.wewe.weweShop.model.ProductDto;
import com.wewe.weweShop.service.ProductService;
import com.wewe.weweShop.util.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminStockController {

    private final ProductService productRepository;

    @Autowired
    public AdminStockController(ProductService productRepository) {
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
                    dto.setStock(p.getStock());
                    return dto;
                })
                .toList();

        int totalStock = products.stream().mapToInt(Product::getStock).sum(); // รวม stock

        model.addAttribute("stockProducts", products);
        model.addAttribute("totalStock", totalStock); // ส่งค่าไปที่ View

        return "admin/stock-view"; // ต้องมีไฟล์ชื่อ stock-view.html อยู่ใน templates/admin/
    }
}

