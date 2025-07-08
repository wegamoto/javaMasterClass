package com.wewe.marketflow.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class CampaignMetric {
    @Id
    @GeneratedValue
    private Long id;

    private String metricType; // เช่น IMPRESSIONS, CLICKS, REACH, ROI, etc.
    private Long value;

    private LocalDate recordDate;

    @ManyToOne
    private Campaign campaign;
}

