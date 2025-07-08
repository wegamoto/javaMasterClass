package com.wewe.temjaisoft.model.account;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "journal_entries")
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate postingDate;

    private String referenceNo;

    private String description;

    @OneToMany(mappedBy = "journalEntry", cascade = CascadeType.ALL)
    private List<LedgerEntry> entries = new ArrayList<>();

    // getters and setters
}

