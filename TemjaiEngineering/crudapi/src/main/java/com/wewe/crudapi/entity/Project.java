package com.wewe.crudapi.entity;

import com.wewe.crudapi.ProjectStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String projectName;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(nullable = false)
    private BigDecimal targetAmount; // เป้าหมายการระดมทุน

    @Column(nullable = false)
    private BigDecimal raisedAmount = BigDecimal.ZERO; // ยอดเงินที่ระดมทุนได้แล้ว

    @Column(nullable = false)
    private BigDecimal estimatedROI; // ผลตอบแทนที่คาดการณ์ (%)

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status; // เช่น ACTIVE, COMPLETED, FAILED

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Investment> investments;

    // Getter & Setter
}

