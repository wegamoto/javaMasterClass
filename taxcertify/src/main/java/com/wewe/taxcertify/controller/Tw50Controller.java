package com.wewe.taxcertify.controller;

import com.wewe.taxcertify.dto.WithholdingTaxForm;
import com.wewe.taxcertify.model.Payee;
import com.wewe.taxcertify.model.Payer;
import com.wewe.taxcertify.model.WithholdingTaxRecord;
import com.wewe.taxcertify.repository.PayeeRepository;
import com.wewe.taxcertify.repository.PayerRepository;
import com.wewe.taxcertify.repository.WithholdingTaxRecordRepository;
import com.wewe.taxcertify.service.PayeeService;
import com.wewe.taxcertify.service.PayerService;
import com.wewe.taxcertify.service.WithholdingTaxService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.wewe.taxcertify.model.WithholdingTaxRecord;

import java.util.List;

@Controller
@RequestMapping("/tw50")
public class Tw50Controller {

    private final WithholdingTaxService taxService;
    private final WithholdingTaxRecordRepository withholdingTaxRecordRepository;
    private final PayeeRepository payeeRepository;
    private final PayerRepository payerRepository;
    private final PayeeService payeeService;
    private final PayerService payerService;

    public Tw50Controller(
            WithholdingTaxService taxService,
            WithholdingTaxRecordRepository withholdingTaxRecordRepository,
            PayeeRepository payeeRepository,
            PayerRepository payerRepository,
            PayeeService payeeService,
            PayerService payerService
    ) {
        this.taxService = taxService;
        this.withholdingTaxRecordRepository = withholdingTaxRecordRepository;
        this.payeeRepository = payeeRepository;
        this.payerRepository = payerRepository;
        this.payeeService = payeeService;
        this.payerService = payerService;
    }

    @GetMapping
    public String showTw50Page(Model model) {
        return "tw50/index";
    }

    @GetMapping("/list")
    public String listRecords(Model model) {
        List<WithholdingTaxRecord> records = taxService.getAllRecords();
        model.addAttribute("records", records);
        return "tw50/list";
    }

    @GetMapping("/new")
    public String newRecordForm(Model model) {
        model.addAttribute("record", new WithholdingTaxForm());
        model.addAttribute("payers", payerService.findAll());
        model.addAttribute("payees", payeeService.findAll());
        return "tw50/form";
    }

    @GetMapping("/edit/{id}")
    public String editRecord(@PathVariable Long id, Model model) {
        WithholdingTaxRecord record = withholdingTaxRecordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid record Id: " + id));

        Long payerId = null;
        Long payeeId = null;

        if (record.getPayer() != null) {
            payerId = record.getPayer().getId();
        } else {
            // อาจจะ log หรือแจ้งเตือนว่าข้อมูลผู้จ่ายไม่ครบ
            System.err.println("Warning: Payer is null for record id: " + id);
        }

        if (record.getPayee() != null) {
            payeeId = record.getPayee().getId();
        } else {
            // อาจจะ log หรือแจ้งเตือนว่าข้อมูลผู้รับเงินไม่ครบ
            System.err.println("Warning: Payee is null for record id: " + id);
        }

        WithholdingTaxForm form = new WithholdingTaxForm(
                payerId,
                payeeId,
                record.getDate(),
                record.getAmount(),
                record.getTaxRate(),
                record.getDescription()
        );

        model.addAttribute("record", form);
        model.addAttribute("payers", payerService.findAll());
        model.addAttribute("payees", payeeService.findAll());

        return "tw50/form";  // ชี้ไปที่หน้าแบบฟอร์มแก้ไข
    }


    @GetMapping("/view/{id}")
    public String viewRecord(@PathVariable Long id, Model model) {
        WithholdingTaxRecord entity = taxService.findById(id);
        if (entity == null) {
            return "redirect:/tw50/list";
        }

        // map entity → DTO
        WithholdingTaxForm form = new WithholdingTaxForm();
        form.setPayerId(entity.getPayer().getId());
        form.setPayeeId(entity.getPayee().getId());
        form.setDate(entity.getDate());
        form.setAmount(entity.getAmount());
        form.setTaxRate(entity.getTaxRate());
        form.setDescription(entity.getDescription());

        model.addAttribute("record", form);
        model.addAttribute("payers", payerService.findAll());
        model.addAttribute("payees", payeeService.findAll());

        return "tw50/form";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        taxService.deleteById(id);
        return "redirect:/tw50/list";
    }

    @PostMapping("/save")
    public String saveRecord(@ModelAttribute WithholdingTaxForm form) {
        Payer payer = payerRepository.findById(form.getPayerId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid payer ID"));
        Payee payee = payeeRepository.findById(form.getPayeeId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid payee ID"));

        WithholdingTaxRecord entity = new WithholdingTaxRecord();
        entity.setPayer(payer);
        entity.setPayee(payee);
        entity.setAmount(form.getAmount());
        entity.setTaxRate(form.getTaxRate());
        entity.setDate(form.getDate());
        entity.setDescription(form.getDescription());

        withholdingTaxRecordRepository.save(entity);

        return "redirect:/tw50/list";
    }

}
