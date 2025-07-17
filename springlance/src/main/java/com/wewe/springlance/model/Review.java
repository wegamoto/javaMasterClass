package com.wewe.springlance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rating; // 1-5
    private String comment;

    @OneToOne
    private ProjectRequest project;

    @ManyToOne
    private User freelancer;  // ✅ ชื่อ field นี้ต้องตรงกับ query

    @ManyToOne
    private User reviewer;

    private LocalDateTime reviewDate;

    // getter/setter
}

