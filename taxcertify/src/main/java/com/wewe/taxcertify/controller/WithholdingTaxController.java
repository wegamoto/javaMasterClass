package com.wewe.taxcertify.controller;

import com.wewe.taxcertify.model.WithholdingTaxRecord;
import com.wewe.taxcertify.model.Payer;
import com.wewe.taxcertify.model.Payee;
import com.wewe.taxcertify.service.WithholdingTaxService;
import com.wewe.taxcertify.service.PayerService;
import com.wewe.taxcertify.service.PayeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/withholding")
public class WithholdingTaxController {

    private final WithholdingTaxService taxService;
    private final PayerService payerService;
    private final PayeeService payeeService;



    public WithholdingTaxController(WithholdingTaxService taxService,
                                    PayerService payerService,
                                    PayeeService payeeService) {
        this.taxService = taxService;
        this.payerService = payerService;
        this.payeeService = payeeService;
    }
}

