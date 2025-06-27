package com.wewe.eduexam.controller;

import com.wewe.eduexam.model.Question;
import com.wewe.eduexam.model.StudentAnswer;
import com.wewe.eduexam.repository.ExamRepository;
import com.wewe.eduexam.repository.StudentAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ResultController {

    @Autowired
    private StudentAnswerRepository studentAnswerRepository;

    @Autowired
    private ExamRepository examRepository;

    @GetMapping("/results")
    public String showResults(Model model) {
        Long studentId = 1L; // สมมุติ: ดึง studentId จาก session ในระบบจริง
        List<StudentAnswer> answers = studentAnswerRepository.findByStudentId(studentId);

        int totalScore = 0;
        int maxScore = 0;

        for (StudentAnswer sa : answers) {
            Question question = sa.getQuestion();
            String correct = question.getCorrectAnswer();
            String student = sa.getTextAnswer();

            if (correct != null && student != null && correct.trim().equalsIgnoreCase(student.trim())) {
                totalScore += question.getFullScore();
            }

            maxScore += question.getFullScore();
        }

        model.addAttribute("answers", answers);
        model.addAttribute("totalScore", totalScore);
        model.addAttribute("maxScore", maxScore);
        return "results"; // results.html
    }
}

