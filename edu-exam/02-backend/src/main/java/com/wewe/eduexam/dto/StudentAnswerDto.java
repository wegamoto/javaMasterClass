package com.wewe.eduexam.dto;

public class StudentAnswerDto {
    private Long id;
    private String examTitle;
    private Double score;

    public StudentAnswerDto(Long id, String examTitle, Double score) {
        this.id = id;
        this.examTitle = examTitle;
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public String getExamTitle() {
        return examTitle;
    }

    public Double getScore() {
        return score;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setExamTitle(String examTitle) {
        this.examTitle = examTitle;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}

