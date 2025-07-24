package com.wewe.taxcertify.service;

import com.wewe.taxcertify.model.Payer;
import com.wewe.taxcertify.repository.PayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayerService {
    private final PayerRepository payerRepository;

    public PayerService(PayerRepository payerRepository) {
        this.payerRepository = payerRepository;
    }

    public List<Payer> findAll() {
        return payerRepository.findAll();
    }

    public Payer save(Payer payer) {
        return payerRepository.save(payer);
    }

    public Payer findById(Long id) {
        return payerRepository.findById(id).orElse(null);
    }
}

