package com.servix.maintenance.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Technician {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String phone;

    private String fullName;

    private String email;

    private String department;

    @Enumerated(EnumType.STRING)
    private SkillLevel skillLevel;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}

