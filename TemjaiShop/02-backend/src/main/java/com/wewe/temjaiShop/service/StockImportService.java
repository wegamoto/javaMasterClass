package com.wewe.temjaiShop.service;

import com.wewe.temjaiShop.model.Category;
import com.wewe.temjaiShop.model.Product;
import com.wewe.temjaiShop.repository.CategoryRepository;
import com.wewe.temjaiShop.repository.ProductRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

@Service
public class StockImportService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public void importStockFromExcel(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // ข้ามแถวหัวตาราง

                String name = getCellValue(row.getCell(0));
                String categoryName = getCellValue(row.getCell(1));
                int stockQuantity = (int) row.getCell(2).getNumericCellValue();
                BigDecimal price = BigDecimal.valueOf(row.getCell(3).getNumericCellValue());

                // ค้นหาสินค้าด้วยชื่อ
                Product product = productRepository.findByName(name).orElse(null);

                if (product == null) {
                    product = new Product();
                    product.setName(name);
                }

                // ตรวจสอบและกำหนดหมวดหมู่
                if (categoryName != null && !categoryName.isEmpty()) {
                    Category category = categoryRepository.findByName(categoryName)
                            .orElseGet(() -> {
                                Category newCat = new Category();
                                newCat.setName(categoryName);
                                return categoryRepository.save(newCat);
                            });
                    product.setCategory(category);
                }

                product.setStockQuantity(stockQuantity);
                product.setPrice(price);

                productRepository.save(product);
            }
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }
}

