package com.wewe.autogate.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class AccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String plateNumber;

    private LocalDateTime accessTime = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private AccessStatus status;

    public enum AccessStatus {
        ALLOWED,
        DENIED
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }
    public LocalDateTime getAccessTime() { return accessTime; }
    public void setAccessTime(LocalDateTime accessTime) { this.accessTime = accessTime; }
    public AccessStatus getStatus() { return status; }
    public void setStatus(AccessStatus status) { this.status = status; }
}

