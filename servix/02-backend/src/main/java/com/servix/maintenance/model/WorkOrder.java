package com.servix.maintenance.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String equipmentName;

    private String description;

    private LocalDateTime scheduledAt;

    private LocalDateTime completedAt;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private WorkOrderStatus status;

    @ManyToOne
    @JoinColumn(name = "machine_id")
    private Machine machine;

    @ManyToOne
    @JoinColumn(name = "technician_id")
    private Technician technician;

    @OneToMany(mappedBy = "workOrder", cascade = CascadeType.ALL)
    private List<SparePartUsage> sparePartsUsed;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private WorkRequest request;
}

