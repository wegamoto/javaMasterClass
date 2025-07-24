package com.wewe.taxcertify.controller;

import com.wewe.taxcertify.model.Payee;
import com.wewe.taxcertify.service.PayeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/payees")
public class PayeeController {

    private final PayeeService payeeService;

    public PayeeController(PayeeService payeeService) {
        this.payeeService = payeeService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("payees", payeeService.findAll());
        return "payee/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("payee", new Payee());
        return "payee/form";
    }

    // บันทึก payee ใหม่ (POST)
    @PostMapping
    public String createPayee(@ModelAttribute Payee payee) {
        payeeService.save(payee);
        return "redirect:/payees/new";
    }

    @GetMapping("/form/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Payee payee = payeeService.findById(id);
        if (payee == null) {
            return "redirect:/payees?notfound"; // redirect ถ้าไม่เจอ
        }
        model.addAttribute("payee", payee);
        return "payee/form";
    }

    // อัพเดต payee (PUT)
    @PutMapping("/{id}")
    public String updatePayee(@PathVariable Long id, @ModelAttribute Payee payee) {
        payee.setId(id);
        payeeService.save(payee);
        return "redirect:/payees";
    }

    // ลบ payee (ถ้าต้องการ)
    @DeleteMapping("/{id}")
    public String deletePayee(@PathVariable Long id) {
        payeeService.deleteById(id);
        return "redirect:/payees";
    }

    // ✅ จุดสำคัญ: แมปสำหรับ POST
    @PostMapping("/save")
    public String save(@ModelAttribute Payee payee) {
        payeeService.save(payee);
        return "redirect:/payees";
    }

    @GetMapping("/reports")
    public String showPayeeReports(Model model) {
        List<Payee> payees = payeeService.findAll();
        model.addAttribute("payees", payees);
        return "payee/report";
    }

}

