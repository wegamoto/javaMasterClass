package com.wewe.proflow.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "cash_flow_entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CashFlow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String type; // "INCOME" หรือ "EXPENSE"

    private String description;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonBackReference
    private Project project;

    @ManyToOne
    @JoinColumn(name = "phase_id")
    @JsonBackReference
    private ProjectPhase phase;

    @ManyToOne
    @JoinColumn(name = "contractor_id")
    private User contractor;
}

