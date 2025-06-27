package com.proman.proman_erp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Machine {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String serialNumber;
    private String location;
    private LocalDate installDate;
    private String status; // ACTIVE, UNDER_REPAIR, DECOMMISSIONED

    @OneToMany(mappedBy = "machine")
    private List<WorkOrder> workOrders;
}

