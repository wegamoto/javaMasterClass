package com.servix.maintenance.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private String requestedBy;

    private LocalDateTime requestDate;

    @Enumerated(EnumType.STRING)
    private WorkRequestStatus status;

    @ManyToOne
    @JoinColumn(name = "machine_id")
    private Machine machine;
}

