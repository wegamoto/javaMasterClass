package com.wewe.promptinvoice.service;

import com.wewe.promptinvoice.model.Invoice;
import com.wewe.promptinvoice.model.InvoiceStatus;
import com.wewe.promptinvoice.repository.InvoiceRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public long countAllInvoices() {
        return invoiceRepository.countAllInvoices();
    }

    public double sumTotalAmount() {
        return invoiceRepository.sumTotalAmount();
    }

    public long countDistinctClients() {
        return invoiceRepository.countDistinctClients();
    }

    public long countOverdueInvoices() {
        return invoiceRepository.countOverdueInvoices();
    }

    public List<Invoice> findRecentInvoices(int limit) {
        return invoiceRepository.findRecentInvoices(PageRequest.of(0, limit));
    }

    // หา Invoice ด้วย id
    public Optional<Invoice> findById(Long id) {
        return invoiceRepository.findById(id);
    }

    public List<Invoice> findAll() {
        return invoiceRepository.findAll();
    }

    public long countInvoicesByStatus(InvoiceStatus status) {
        return invoiceRepository.countByStatus(status);
    }

    public List<Invoice> findInvoicesByDateRange(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        return invoiceRepository.findByCreatedAtBetween(startDateTime, endDateTime);
    }
}

