package com.wewe.taxcertify.service;

import com.wewe.taxcertify.model.Payee;
import com.wewe.taxcertify.repository.PayeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayeeService {
    private final PayeeRepository payeeRepository;

    public PayeeService(PayeeRepository payeeRepository) {
        this.payeeRepository = payeeRepository;
    }

    public List<Payee> findAll() {
        return payeeRepository.findAll();
    }

    public Payee save(Payee payee) {
        return payeeRepository.save(payee);
    }

    public Payee findById(Long id) {
        return payeeRepository.findById(id).orElse(null);
    }

    // เพิ่มเมธอดลบตาม id
    public void deleteById(Long id) {
        if (!payeeRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. Payee not found with id " + id);
        }
        payeeRepository.deleteById(id);
    }
}

