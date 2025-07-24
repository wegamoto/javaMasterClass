package com.wewe.springlance.model;

import com.wewe.springlance.model.enums.InvoiceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    private LocalDate issueDate;

    private Boolean isPaid;

    private LocalDate issuedDate;

    @OneToOne
    private ProjectRequest project;

    @ManyToOne
    private User freelancer;

    @ManyToOne
    private User client;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceStatus status;

    // getter/setter
}

