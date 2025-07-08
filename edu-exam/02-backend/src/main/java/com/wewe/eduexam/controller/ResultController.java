package com.wewe.eduexam.controller;

import com.wewe.eduexam.model.Question;
import com.wewe.eduexam.model.Student;
import com.wewe.eduexam.model.StudentAnswer;
import com.wewe.eduexam.repository.ExamRepository;
import com.wewe.eduexam.repository.StudentAnswerRepository;
import com.wewe.eduexam.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class ResultController {

    @Autowired
    private StudentAnswerRepository studentAnswerRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ExamRepository examRepository;

    @GetMapping("/results")
    public String showResults(Model model, Authentication authentication) {
        String username = authentication.getName();

        // สมมุติว่าคุณมี method หา Student จาก username
        Optional<Student> studentOpt = studentRepository.findByUserUsername(username);

        if (studentOpt.isEmpty()) {
            // กรณี user ไม่ใช่ student เช่น admin
            // จะ redirect ไปหน้าอื่น หรือแสดง error ก็ได้
            return "redirect:/access-denied";
            // หรือ return "error-no-student"; ถ้ามีหน้า error แยก
        }

        Student student = studentOpt.get();
        Long studentId = student.getId();

        List<StudentAnswer> answers = studentAnswerRepository.findByStudentId(studentId);

        int totalScore = 0;
        int maxScore = 0;

        for (StudentAnswer sa : answers) {
            Question question = sa.getQuestion();
            String correct = question.getCorrectAnswer();
            String studentAnswer = sa.getTextAnswer();

            if (correct != null && studentAnswer != null && correct.trim().equalsIgnoreCase(studentAnswer.trim())) {
                totalScore += question.getFullScore();
            }

            maxScore += question.getFullScore();
        }

        model.addAttribute("answers", answers);
        model.addAttribute("totalScore", totalScore);
        model.addAttribute("maxScore", maxScore);
        return "results"; // results-old.html
    }
}

