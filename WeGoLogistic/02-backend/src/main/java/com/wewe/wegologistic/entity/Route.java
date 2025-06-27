package com.wewe.wegologistic.entity;

import jakarta.persistence.*;

@Entity
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String origin;
    private String destination;
    private double distanceKm;

    // Getter/Setter
    // ...
}

