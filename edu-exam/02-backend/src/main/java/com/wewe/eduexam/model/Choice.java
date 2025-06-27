package com.wewe.eduexam.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "choices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Choice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private int number = 1;  // ค่า default ที่ Builder จะนำมาใช้

    private String content;

    // ✅ ต้องมี getter ด้วย
    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect = false;  // default Java-side

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;  // ใช้ Question จาก package ของเราเอง

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }
}
