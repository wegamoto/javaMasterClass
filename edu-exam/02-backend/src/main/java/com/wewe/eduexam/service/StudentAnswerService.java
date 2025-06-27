package com.wewe.eduexam.service;

import com.wewe.eduexam.model.*;
import com.wewe.eduexam.repository.StudentAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentAnswerService {

    private final StudentAnswerRepository studentAnswerRepository;

    public List<StudentAnswer> getAnswersForExamAndStudent(User student, Exam exam) {
        return studentAnswerRepository.findByStudentAndExam(Optional.ofNullable(student), exam);
    }

    public double calculateScore(StudentAnswer studentAnswer) {
        Question question = studentAnswer.getQuestion();

        // ปรนัย: ตรวจจาก choice
        if (question.isMultipleChoice()) {
            Choice selected = studentAnswer.getSelectedChoice();
            if (selected != null && selected.getIsCorrect()) {
                return question.getFullScore();  // คะแนนเต็มของคำถาม
            } else {
                return 0.0;
            }
        }

        // อัตนัย: รอครูตรวจ หรือให้ AI ประเมินภายหลัง
        if (question.isTextAnswer()) {
            return studentAnswer.getScore() != null ? studentAnswer.getScore() : 0.0;
        }

        return 0.0;
    }

    public void gradeAndSave(StudentAnswer studentAnswer) {
        double score = calculateScore(studentAnswer);
        studentAnswer.setScore(score);
        studentAnswerRepository.save(studentAnswer);
    }

}
