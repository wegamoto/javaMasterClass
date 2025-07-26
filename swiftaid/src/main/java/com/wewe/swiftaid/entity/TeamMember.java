package com.wewe.swiftaid.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "กรุณากรอกชื่อ")
    private String name;

    @NotBlank(message = "กรุณากรอกบทบาท")
    private String role;

    @NotNull(message = "กรุณาเลือกทีม")
    @ManyToOne
    @JoinColumn(name = "team_id")
    private AmbulanceTeam team;
}

