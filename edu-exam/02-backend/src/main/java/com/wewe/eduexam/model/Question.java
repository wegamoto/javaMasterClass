package com.wewe.eduexam.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"exam", "choices", "options"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank(message = "กรุณากรอกข้อความคำถาม")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    @JsonIgnore
    private Exam exam;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Choice> choices = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Option> options = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @NotNull(message = "กรุณาระบุประเภทคำถาม")
    @Column(name = "type", nullable = false)
    private QuestionType type;

    @Column(name = "full_score", nullable = false)
    private Double fullScore;

    // ✅ เพิ่มคำตอบที่ถูกต้อง
    @Column(name = "correct_answer", columnDefinition = "TEXT")
    private String correctAnswer;  // เผื่อแบบอัตนัย

    public Question(Long questionId) {
        this.id = questionId;
    }

    public void addChoice(Choice choice) {
        choices.add(choice);
        choice.setQuestion(this);
    }

    public void removeChoice(Choice choice) {
        choices.remove(choice);
        choice.setQuestion(null);
    }

    public void addOption(Option option) {
        options.add(option);
        option.setQuestion(this);
    }

    public void removeOption(Option option) {
        options.remove(option);
        option.setQuestion(null);
    }

    public boolean isMultipleChoice() {
        return this.type == QuestionType.MULTIPLE_CHOICE;
    }

    public boolean isTextAnswer() {
        return this.type == QuestionType.TEXT;
    }

    public String getCorrectAnswer() {
        if (isTextAnswer()) {
            return correctAnswer;
        } else if (isMultipleChoice()) {
            return choices.stream()
                    .filter(Choice::getCorrect)
                    .map(Choice::getContent)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

}
