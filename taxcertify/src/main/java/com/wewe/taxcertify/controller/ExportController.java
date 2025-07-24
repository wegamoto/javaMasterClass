package com.wewe.taxcertify.controller;

import com.wewe.taxcertify.model.WithholdingTaxRecord;
import com.wewe.taxcertify.service.WithholdingTaxService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Controller
public class ExportController {

    private final WithholdingTaxService taxService;

    public ExportController(WithholdingTaxService taxService) {
        this.taxService = taxService;
    }

    @GetMapping("/tw50/export/excel")
    public ResponseEntity<byte[]> exportExcel() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("TW50 Report");

            // กำหนดขนาดคอลัมน์
            sheet.setColumnWidth(0, 4000);  // ลำดับที่
            sheet.setColumnWidth(1, 7000);  // ผู้มีหน้าที่หักภาษี
            sheet.setColumnWidth(2, 6000);  // เลขประจำตัวผู้เสียภาษี (Payer)
            sheet.setColumnWidth(3, 7000);  // ผู้ถูกหักภาษี
            sheet.setColumnWidth(4, 6000);  // เลขประจำตัวผู้เสียภาษี (Payee)
            sheet.setColumnWidth(5, 7000);  // ประเภทเงินได้
            sheet.setColumnWidth(6, 4000);  // วันที่จ่าย
            sheet.setColumnWidth(7, 5000);  // จำนวนเงินที่จ่าย
            sheet.setColumnWidth(8, 5000);  // ภาษีที่หัก
            sheet.setColumnWidth(9, 4000);  // ลำดับที่แบบภาษี
            sheet.setColumnWidth(10, 8000); // รายละเอียด / หมายเหตุ

            // ฟอนต์หัวข้อแบบหนา
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short)12);

            // สไตล์หัวข้อ
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            // สไตล์ข้อมูลทั่วไป
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // สไตล์วันที่
            CreationHelper createHelper = workbook.getCreationHelper();
            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.cloneStyleFrom(dataStyle);
            dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));

            // สไตล์จำนวนเงิน
            CellStyle currencyStyle = workbook.createCellStyle();
            currencyStyle.cloneStyleFrom(dataStyle);
            currencyStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0.00"));
            currencyStyle.setAlignment(HorizontalAlignment.RIGHT);

            // หัวตาราง
            // Font and styles
            Font boldFont = workbook.createFont();
            boldFont.setBold(true);
            CellStyle boldStyle = workbook.createCellStyle();
            boldStyle.setFont(boldFont);

            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);

            // Row 0: Company Name (merged)
            Row row0 = sheet.createRow(0);
            Cell companyCell = row0.createCell(0);
            companyCell.setCellValue("บริษัท เต็มใจเอ็นจิเนียริ่ง จำกัด");
            companyCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 4));

            // Row 1: Form Title (merged)
            Row row1 = sheet.createRow(1);
            Cell titleCell = row1.createCell(0);
            titleCell.setCellValue("แบบฟอร์ม TW50 รายงานการทำงานประจำวัน");
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(1, 1, 0, 4));

            // สร้าง header
            int rowIndex = 10; // เริ่มจากแถวที่ 10
            Row headerRow = sheet.createRow(rowIndex);
            String[] headers = {
                    "ลำดับที่",
                    "ผู้มีหน้าที่หักภาษี ณ ที่จ่าย",
                    "เลขประจำตัวผู้เสียภาษี (Payer)",
                    "ผู้ถูกหักภาษี ณ ที่จ่าย",
                    "เลขประจำตัวผู้เสียภาษี (Payee)",
                    "ประเภทเงินได้",
                    "วันที่จ่าย",
                    "จำนวนเงินที่จ่าย",
                    "ภาษีที่หักและนำส่ง",
                    "ลำดับที่แบบภาษี",
                    "รายละเอียด"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // ดึงข้อมูลจาก service
            List<WithholdingTaxRecord> records = taxService.findRecent();
            rowIndex++;
            int rowNum = rowIndex;
            int sequence = 1;
            for (WithholdingTaxRecord record : records) {
                Row row = sheet.createRow(rowNum++);

                // ลำดับที่
                Cell seqCell = row.createCell(0);
                seqCell.setCellValue(sequence++);
                seqCell.setCellStyle(dataStyle);

                // ผู้มีหน้าที่หักภาษี
                Cell payerNameCell = row.createCell(1);
                payerNameCell.setCellValue(record.getPayer().getName());
                payerNameCell.setCellStyle(dataStyle);

                // เลขประจำตัวผู้เสียภาษี (Payer)
                Cell payerTaxIdCell = row.createCell(2);
                payerTaxIdCell.setCellValue(record.getPayer().getTaxId());
                payerTaxIdCell.setCellStyle(dataStyle);

                // ผู้ถูกหักภาษี
                Cell payeeNameCell = row.createCell(3);
                payeeNameCell.setCellValue(record.getPayee().getName());
                payeeNameCell.setCellStyle(dataStyle);

                // เลขประจำตัวผู้เสียภาษี (Payee)
                Cell payeeTaxIdCell = row.createCell(4);
                payeeTaxIdCell.setCellValue(record.getPayee().getTaxId());
                payeeTaxIdCell.setCellStyle(dataStyle);

                // ประเภทเงินได้
                Cell incomeTypeCell = row.createCell(5);
                incomeTypeCell.setCellValue(record.getIncomeType());
                incomeTypeCell.setCellStyle(dataStyle);

                // วันที่จ่าย
                Cell dateCell = row.createCell(6);
                dateCell.setCellValue(java.sql.Date.valueOf(record.getDate()));
                dateCell.setCellStyle(dateStyle);

                // จำนวนเงินที่จ่าย
                Cell amountCell = row.createCell(7);
                amountCell.setCellValue(record.getAmount().doubleValue());
                amountCell.setCellStyle(currencyStyle);

                // ภาษีที่หักและนำส่ง
                Cell taxCell = row.createCell(8);
                taxCell.setCellValue(record.getTaxAmount().doubleValue());
                taxCell.setCellStyle(currencyStyle);

                // ลำดับที่แบบภาษี
                Cell formSeqCell = row.createCell(9);

                formSeqCell.setCellValue(record.getTaxFormSequence() != null ? record.getTaxFormSequence() : 0);

                formSeqCell.setCellStyle(dataStyle);

                // รายละเอียด / หมายเหตุ
                Cell detailCell = row.createCell(10);
                detailCell.setCellValue(record.getDescription());
                detailCell.setCellStyle(dataStyle);
            }

            String filename = "TW50_Report.xlsx";
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(out.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
