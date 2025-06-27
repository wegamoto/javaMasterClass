package com.wewe.eduexam.dto;

import com.wewe.eduexam.model.Exam;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResultDTO {

    private String studentName;

    private Exam exam;

    private String examTitle;

    private String questionText;

    private String choiceText;

    private String textAnswer;

    private Double score;

    private Double maxScore;

    private LocalDateTime submittedAt;

    // ✅ constructor minimal สำหรับชื่อ + ข้อสอบ + คะแนน
    public StudentResultDTO(String studentName, String examTitle, Double score, Double maxScore) {
        this.studentName = studentName;
        this.examTitle = examTitle;
        this.score = score;
        this.maxScore = maxScore;
    }

    // ✅ constructor เดิมสำหรับ case ที่ใช้แค่ชื่อ/ชื่อข้อสอบ/score
    public StudentResultDTO(String studentName, @NotBlank String examTitle, double score) {
        this.studentName = studentName;
        this.examTitle = examTitle;
        this.score = score;
    }

    // ✅ constructor แบบเต็มกรณีใช้งาน detail แต่ไม่มี Builder
    public StudentResultDTO(String studentName, Exam exam, String questionText, String choiceText, String textAnswer,
                            Double score, Double maxScore, LocalDateTime submittedAt) {
        this.studentName = studentName;
        this.exam = exam;
        this.examTitle = (exam != null) ? exam.getTitle() : null;
        this.questionText = questionText;
        this.choiceText = choiceText;
        this.textAnswer = textAnswer;
        this.score = score;
        this.maxScore = maxScore;
        this.submittedAt = submittedAt;
    }
}
