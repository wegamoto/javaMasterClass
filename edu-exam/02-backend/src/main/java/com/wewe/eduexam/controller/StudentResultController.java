package com.wewe.eduexam.controller;

import com.wewe.eduexam.dto.StudentResultDTO;
import com.wewe.eduexam.model.*;
import com.wewe.eduexam.repository.StudentAnswerRepository;
import com.wewe.eduexam.repository.StudentRepository;
import com.wewe.eduexam.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student")
public class StudentResultController {

    private static final Logger log = LoggerFactory.getLogger(StudentResultController.class);

    private final StudentAnswerRepository studentAnswerRepository;
    private final UserService userService;
    private final StudentRepository studentRepository;

    public StudentResultController(StudentAnswerRepository studentAnswerRepository,
                                   UserService userService, StudentRepository studentRepository) {
        this.studentAnswerRepository = studentAnswerRepository;
        this.userService = userService;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/results")
    public String showResults(Model model, Authentication authentication) {
        String username = authentication.getName();

        Optional<Student> studentOpt = studentRepository.findByUserUsername(username);

        if (studentOpt.isEmpty()) {
            model.addAttribute("errorMessage", "ไม่พบ Student ของผู้ใช้: " + username);
            return "error"; // หรือ redirect ไปหน้าอื่น เช่น return "redirect:/";
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
        return "results";
    }

}

