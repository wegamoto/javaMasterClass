package com.wewe.weweShop.controller;

import com.wewe.weweShop.model.Product;
import com.wewe.weweShop.service.ProductService;
import com.wewe.weweShop.service.export.ExcelExporter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private ProductService productService;

    @GetMapping("/view")
    @PreAuthorize("hasRole('ADMIN')")
    public String viewStock(@RequestParam(name = "keyword", required = false) String keyword,
                            @RequestParam(name = "lowStockOnly", required = false, defaultValue = "false") boolean lowStockOnly,
                            Model model) {

        List<Product> products;

        if (keyword != null && !keyword.isEmpty()) {
            products = productService.searchProductsByNameOrCategory(keyword);
        } else {
            products = productService.getAllProducts();
        }

        if (lowStockOnly) {
            products = products.stream()
                    .filter(p -> p.getStock() < 5)
                    .collect(Collectors.toList());
        }

        model.addAttribute("stockProducts", products);
        model.addAttribute("keyword", keyword);
        model.addAttribute("lowStockOnly", lowStockOnly);
        return "admin/stock-view";
    }

    @GetMapping("/export")
    @PreAuthorize("hasRole('ADMIN')")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=stock_products.xlsx";
        response.setHeader(headerKey, headerValue);

        List<Product> products = productService.getAllProducts();
        ExcelExporter exporter = new ExcelExporter(products);
        exporter.export(response);
    }
}

