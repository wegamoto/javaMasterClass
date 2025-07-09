package com.wewe.proflow.controller.web;

import com.wewe.proflow.model.CashFlow;
import com.wewe.proflow.model.Role;
import com.wewe.proflow.service.CashFlowService;
import com.wewe.proflow.service.ProjectService;
import com.wewe.proflow.service.ProjectPhaseService;
import com.wewe.proflow.service.UserService;
import com.wewe.proflow.service.export.ExcelExportService;
import com.wewe.proflow.service.export.PdfExportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/cashflows")
@RequiredArgsConstructor
public class CashFlowController {

    private final CashFlowService cashFlowService;
    private final ProjectService projectService;
    private final ProjectPhaseService projectPhaseService;
    private final UserService userService;
    private final PdfExportService pdfExportService;
    private final ExcelExportService excelExportService;

    @GetMapping
    public String listCashFlows(Model model) {
        model.addAttribute("cashflows", cashFlowService.findAll());
        return "cashflows/cashflow"; // cashflow.html
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        CashFlow cashFlow = new CashFlow();
        model.addAttribute("cashFlow", cashFlow);
        loadFormData(model);
        return "cashflows/cashflow-form";
    }

    @PostMapping("/save")
    public String saveCashFlow(@ModelAttribute("cashFlow") CashFlow cashFlow) {
        cashFlowService.save(cashFlow);
        return "redirect:/cashflows";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        CashFlow cashFlow = cashFlowService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid CashFlow ID: " + id));
        model.addAttribute("cashFlow", cashFlow);
        loadFormData(model);
        return "cashflows/cashflow-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteCashFlow(@PathVariable Long id) {
        cashFlowService.deleteById(id);
        return "redirect:/cashflows";
    }

    // 🔁 แชร์ข้อมูล Project, Phase, Contractor
    private void loadFormData(Model model) {
        model.addAttribute("projects", projectService.findAll());
        model.addAttribute("phases", projectPhaseService.findAll());
        model.addAttribute("users", userService.findByRole(Role.CONTRACTOR)); // filter เฉพาะ contractor
    }

    @GetMapping("/report")
    public String showReport(Model model) {
        List<CashFlow> cashFlows = cashFlowService.findAll();

        model.addAttribute("cashFlows", cashFlows);
        return "cashflows/cashflow-report"; // ไฟล์ HTML ชื่อ cashflow-report.html
    }

    @GetMapping("/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=cashflows.pdf");

        List<CashFlow> cashFlows = cashFlowService.findAll();
        pdfExportService.export(response.getOutputStream(), cashFlows);
    }

    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=cashflows.xlsx");

        List<CashFlow> cashFlows = cashFlowService.findAll();
        excelExportService.export(response.getOutputStream(), cashFlows);
    }



}
