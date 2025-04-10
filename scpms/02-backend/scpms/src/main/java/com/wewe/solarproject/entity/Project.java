package com.wewe.solarproject.entity;

import com.wewe.solarproject.ProjectStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String location;

    private BigDecimal budget;
    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "project")
    private List<Expense> expenses;

    @OneToMany(mappedBy = "project")
    private List<ProgressReport> progressReports;

    @OneToMany(mappedBy = "project")
    private List<MaterialUsage> materialUsages;

    @OneToMany(mappedBy = "project")
    private List<ProjectTeamAssignment> teamAssignments;
}

