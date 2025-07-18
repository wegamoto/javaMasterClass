package com.wewe.springlance.service.impl;

import com.wewe.springlance.model.Invoice;
import com.wewe.springlance.model.User;
import com.wewe.springlance.repository.InvoiceRepository;
import com.wewe.springlance.service.InvoiceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceServiceImpl(InvoiceRepository repository) {
        this.invoiceRepository = repository;
    }

    @Override
    public Invoice save(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    @Override
    public Optional<Invoice> findById(Long id) {
        return invoiceRepository.findById(id);
    }

    @Override
    public Invoice findByProjectId(Long projectId) {
        return invoiceRepository.findByProjectId(projectId);
    }

    @Override
    public List<Invoice> getInvoicesForUser(User user) {
        // สามารถรวมทั้งเป็น client หรือ freelancer
        List<Invoice> asClient = invoiceRepository.findByProject_Client(user);
        List<Invoice> asFreelancer = invoiceRepository.findByProject_Freelancer(user);
        asClient.addAll(asFreelancer);
        return asClient;
    }
}
