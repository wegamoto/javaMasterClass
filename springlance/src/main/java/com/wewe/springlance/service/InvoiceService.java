package com.wewe.springlance.service;

import com.wewe.springlance.model.Invoice;
import com.wewe.springlance.model.User;

import java.util.List;
import java.util.Optional;

public interface InvoiceService {
    Invoice save(Invoice invoice);
    Optional<Invoice> findById(Long id);
    Invoice findByProjectId(Long projectId);
    List<Invoice> getInvoicesForUser(User user);
}
