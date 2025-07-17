package com.wewe.promptinvoice.service;

import com.wewe.promptinvoice.model.Invoice;
import com.wewe.promptinvoice.model.Payment;
import com.wewe.promptinvoice.repository.InvoiceRepository;
import com.wewe.promptinvoice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PaymentService {
    @Autowired private PaymentRepository paymentRepo;
    @Autowired
    private InvoiceRepository invoiceRepo;

    public Payment addPayment(Long invoiceId, BigDecimal amount, String method) {
        Invoice invoice = invoiceRepo.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        Payment payment = new Payment();
        payment.setInvoice(invoice);
        payment.setAmount(amount);
        payment.setMethod(method);
        payment.setPaidAt(LocalDateTime.now());

        return paymentRepo.save(payment);
    }
}

