package com.wewe.swiftaid.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class AmbulanceTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String contactNumber;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<TeamMember> members;
}
