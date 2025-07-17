package com.wewe.springlance.service.impl;

import com.wewe.springlance.model.Invoice;
import com.wewe.springlance.repository.InvoiceRepository;
import com.wewe.springlance.service.InvoiceService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository repository;

    public InvoiceServiceImpl(InvoiceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Invoice save(Invoice invoice) {
        return repository.save(invoice);
    }

    @Override
    public Optional<Invoice> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Invoice findByProjectId(Long projectId) {
        return repository.findByProjectId(projectId);
    }
}
