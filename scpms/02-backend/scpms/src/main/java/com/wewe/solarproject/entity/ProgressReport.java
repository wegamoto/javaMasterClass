package com.wewe.solarproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate reportDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer progressPercent;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}

