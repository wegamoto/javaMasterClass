package com.wewe.weweShop.service.export;


import com.wewe.weweShop.model.Product;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.IOException;
import java.util.List;

public class ExcelExporter {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Product> productList;

    public ExcelExporter(List<Product> productList) {
        this.productList = productList;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Stock");
    }

    private void writeHeaderRow() {
        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        String[] headers = {"Product Name", "Category", "Price", "Stock"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }
    }

    private void writeDataRows() {
        int rowCount = 1;
        for (Product product : productList) {
            Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue(product.getName());
            row.createCell(1).setCellValue((RichTextString) product.getCategory());
            row.createCell(2).setCellValue((RichTextString) product.getPrice());
            row.createCell(3).setCellValue(product.getStock());
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderRow();
        writeDataRows();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}

