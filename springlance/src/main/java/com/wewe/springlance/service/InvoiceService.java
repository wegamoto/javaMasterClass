package com.wewe.springlance.service;

import com.wewe.springlance.model.Invoice;

import java.util.Optional;

public interface InvoiceService {
    Invoice save(Invoice invoice);
    Optional<Invoice> findById(Long id);
    Invoice findByProjectId(Long projectId);
}
