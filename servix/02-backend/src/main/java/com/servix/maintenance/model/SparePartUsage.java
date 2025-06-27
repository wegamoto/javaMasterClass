package com.servix.maintenance.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SparePartUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantityUsed;

    @ManyToOne
    @JoinColumn(name = "spare_part_id")
    private SparePart sparePart;

    @ManyToOne
    @JoinColumn(name = "work_order_id")
    private WorkOrder workOrder;
}

