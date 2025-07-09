package com.wewe.proflow.service.export;

import com.wewe.proflow.model.CashFlow;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelExportService {

    public void export(OutputStream outputStream, List<CashFlow> cashFlows) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("CashFlows");

            // Header row
            Row header = sheet.createRow(0);
            String[] columns = {"Date", "Project", "Phase", "Contractor", "Type", "Amount", "Status", "Description"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(getHeaderStyle(workbook));
            }

            // Data rows
            int rowIdx = 1;
            DecimalFormat df = new DecimalFormat("#,##0.00");

            for (CashFlow cf : cashFlows) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(cf.getDate().toString());
                row.createCell(1).setCellValue(cf.getProject().getName());
                row.createCell(2).setCellValue(cf.getPhase() != null ? cf.getPhase().getName() : "—");
                row.createCell(3).setCellValue(cf.getContractor() != null ? cf.getContractor().getName() : "—");
                row.createCell(4).setCellValue(cf.getType().toString());
                row.createCell(5).setCellValue(cf.getAmount());
                row.createCell(6).setCellValue(cf.getStatus().toString());
                row.createCell(7).setCellValue(cf.getDescription());
            }

            // Auto-size columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private CellStyle getHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }
}

