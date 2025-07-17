package com.wewe.springlance.model;

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

    // getter/setter
}

