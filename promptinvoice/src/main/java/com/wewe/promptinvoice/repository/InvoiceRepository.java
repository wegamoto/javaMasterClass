package com.wewe.promptinvoice.repository;

import com.wewe.promptinvoice.model.Invoice;
import com.wewe.promptinvoice.model.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query("SELECT COUNT(i) FROM Invoice i")
    long countAllInvoices();

    @Query("SELECT COALESCE(SUM(i.totalAmount), 0) FROM Invoice i")
    double sumTotalAmount();

    @Query("SELECT COUNT(DISTINCT i.clientName) FROM Invoice i")
    long countDistinctClients();

    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.status = 'ค้างชำระ'")
    long countOverdueInvoices();

    @Query("SELECT i FROM Invoice i ORDER BY i.createdAt DESC")
    List<Invoice> findRecentInvoices(org.springframework.data.domain.Pageable pageable);

    long countByStatus(InvoiceStatus status);

    List<Invoice> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT i FROM Invoice i WHERE i.createdAt BETWEEN :start AND :end")
    List<Invoice> findByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}

