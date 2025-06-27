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
    public String showResults(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User studentUser = userService.findByUsername(username);
        if (studentUser == null) {
            throw new UsernameNotFoundException("ไม่พบผู้ใช้: " + username);
        }

        // ดึง Student จาก username ของ User
        Student student = studentRepository.findByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("ไม่พบ Student ของผู้ใช้: " + username));

        // ดึง StudentAnswer โดยใช้ Student (ถูกต้อง)
        List<StudentAnswer> studentAnswers = studentAnswerRepository.findByStudent(student);

        // จัดกลุ่มตาม Exam
        Map<Exam, List<StudentAnswer>> groupedByExam = studentAnswers.stream()
                .collect(Collectors.groupingBy(StudentAnswer::getExam));

        List<StudentResultDTO> results = new ArrayList<>();

        for (Map.Entry<Exam, List<StudentAnswer>> entry : groupedByExam.entrySet()) {
            Exam exam = entry.getKey();
            List<StudentAnswer> answers = entry.getValue();

            double totalScore = 0.0;
            double maxScore = 0.0;

            for (StudentAnswer sa : answers) {
                Double score = (sa.getScore() != null) ? sa.getScore() : 0.0;
                totalScore += score;

                Question q = sa.getQuestion();
                if (q != null && q.getFullScore() != null) {
                    maxScore += q.getFullScore();
                }
            }

            results.add(new StudentResultDTO(
                    student.getName(),
                    exam.getTitle(),
                    totalScore,
                    maxScore
            ));
        }

        model.addAttribute("results", results);
        return "results";
    }

}

