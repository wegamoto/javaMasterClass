package com.wewe.swiftaid.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class EmergencyCase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private String location;
    private LocalDateTime reportedAt;

    @Enumerated(EnumType.STRING)
    private EmergencyStatus status;

    @ManyToOne
    private AmbulanceTeam team;
}
