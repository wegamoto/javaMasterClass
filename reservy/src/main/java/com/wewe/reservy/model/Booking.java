package com.wewe.reservy.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

// model/Booking.java
@Entity
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDate date;

    private LocalTime time;

    private String serviceType;

    // ✅ เลขคิว เช่น Q001
    @Column(unique = true)
    private String queueNumber;

    // ✅ สถานะของคิว
    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.PENDING;

    // ✅ วันที่เวลาจองอัตโนมัติ
    @CreationTimestamp
    private LocalDateTime createdAt;

    // Getter, Setter
}

