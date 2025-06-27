package com.wewe.eduexam.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "student_answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ ผู้เรียน
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    // ✅ ข้อสอบ
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    // ✅ คำถาม
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    // ✅ คำตอบแบบปรนัย (หากเป็น Choice)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "selected_choice_id")
    private Choice selectedChoice;

    // ✅ คำตอบแบบเขียน
    @Column(columnDefinition = "TEXT")
    private String textAnswer;

    // ✅ คะแนนที่ได้ในข้อนี้ (ถ้ามี)
    private Double score;

    private LocalDateTime submittedAt;  // เพิ่มฟิลด์นี้สำหรับบันทึกเวลาส่งคำตอบ

    public void setCorrect(boolean b) {
    }
}
