package com.wewe.taxcertify.service;

import com.wewe.taxcertify.model.WithholdingTaxRecord;
import com.wewe.taxcertify.repository.WithholdingTaxRecordRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WithholdingTaxService {
    private final WithholdingTaxRecordRepository repository;

    public WithholdingTaxService(WithholdingTaxRecordRepository repository) {
        this.repository = repository;
    }

    public List<WithholdingTaxRecord> findAll() {
        return repository.findAll();
    }

    public List<WithholdingTaxRecord> findRecent() {
        return repository.findTop5ByOrderByDateDesc();
    }

    public long count() {
        return repository.count();
    }

    public BigDecimal sumTax() {
        BigDecimal sum = repository.sumTotalTax();
        return sum != null ? sum : BigDecimal.ZERO;
    }

    public WithholdingTaxRecord save(WithholdingTaxRecord record) {
        return repository.save(record);
    }

    public WithholdingTaxRecord findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<WithholdingTaxRecord> getAllRecords() {
        return repository.findAll();
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}

