package com.proman.proman_erp.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class WorkRequest {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private LocalDateTime requestDate;
    private String requestedBy;
    private WorkRequestStatus status; // NEW, APPROVED, REJECTED

    @ManyToOne
    private Machine machine;
}

