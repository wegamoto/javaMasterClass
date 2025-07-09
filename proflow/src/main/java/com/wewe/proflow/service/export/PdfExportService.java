package com.wewe.proflow.service.export;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.UnitValue;
import com.wewe.proflow.model.CashFlow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfExportService {

    public void export(OutputStream outputStream, List<CashFlow> cashFlows) {
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        try {
            document.add(new Paragraph("Cash Flow Report")
                    .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                    .setFontSize(18)
                    .setMarginBottom(10));

            Table table = new Table(new float[]{2, 3, 3, 3, 2, 2, 2, 4});
            table.setWidth(UnitValue.createPercentValue(100));

            String[] headers = {"Date", "Project", "Phase", "Contractor", "Type", "Amount", "Status", "Description"};
            for (String header : headers) {
                table.addHeaderCell(new Cell().add(new Paragraph(header).setBold()));
            }

            DecimalFormat df = new DecimalFormat("#,##0.00");

            for (CashFlow cf : cashFlows) {
                table.addCell(cf.getDate().toString());
                table.addCell(cf.getProject().getName());
                table.addCell(cf.getPhase() != null ? cf.getPhase().getName() : "—");
                table.addCell(cf.getContractor() != null ? cf.getContractor().getName() : "—");
                table.addCell(cf.getType().toString());
                table.addCell(df.format(cf.getAmount()));
                table.addCell(cf.getStatus().toString());
                table.addCell(cf.getDescription());
            }

            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
}

