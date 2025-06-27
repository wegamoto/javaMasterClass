package com.wewe.eduexam.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentName; // หรือ userId ถ้ามีระบบ Login

    private Double score;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL)
    private List<Answer> answers;
}

