package com.wewe.promptinvoice.model;

import jakarta.persistence.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Invoice {

    // --- Getter / Setter ---
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientName;

    private String clientEmail;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    private boolean vatApplied;

    private double totalAmount;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    @Lob
    private String itemsJson; // รายการสินค้า/บริการ เป็น JSON string (เช่น [{"desc":"Design","price":5000}])

    public String getTotalAmountFormatted() {
        return String.format("%,.2f", totalAmount);
    }

}

