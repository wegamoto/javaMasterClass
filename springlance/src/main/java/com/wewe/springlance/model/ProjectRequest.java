package com.wewe.springlance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProjectRequest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 5000)
    private String description;

    private String status; // REQUESTED, ACCEPTED, IN_PROGRESS, COMPLETED

    private Double budget;

    private LocalDate deadline;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @ManyToOne
    private User freelancer;

    @OneToMany(mappedBy = "project")
    private List<Message> messages;

    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL)
    private Invoice invoice;

    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL)
    private Review review;

    private LocalDateTime createdAt;

    // getter/setter
}

