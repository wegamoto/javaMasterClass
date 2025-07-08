package com.wewe.eduexam.controller;

import com.wewe.eduexam.model.*;
import com.wewe.eduexam.repository.*;
import com.wewe.eduexam.service.ExamService;
import com.wewe.eduexam.service.StudentExamResultService;
import com.wewe.eduexam.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/student")
public class StudentExamController {

    private final ExamRepository examRepository;
    private final ExamService examService;
    private final UserRepository userRepository;
    private final StudentAnswerRepository studentAnswerRepository;
    private final StudentService studentService;
    private final ChoiceRepository choiceRepository;
    private final StudentExamResultService studentExamResultService;

    public StudentExamController(
            ExamRepository examRepository,
            ExamService examService,
            UserRepository userRepository,
            StudentAnswerRepository studentAnswerRepository,
            StudentService studentService,
            ChoiceRepository choiceRepository,
            StudentExamResultService studentExamResultService
    ) {
        this.examRepository = examRepository;
        this.examService = examService;
        this.userRepository = userRepository;
        this.studentAnswerRepository = studentAnswerRepository;
        this.studentService = studentService;
        this.choiceRepository = choiceRepository;
        this.studentExamResultService = studentExamResultService;
    }

    // ✅ รายการข้อสอบ
    @GetMapping("/exams")
    public String showAvailableExams(Model model) {
        List<Exam> exams = examRepository.findAll();
        model.addAttribute("exams", exams);
        return "student_exams";
    }

    // ✅ เริ่มทำข้อสอบ
    @GetMapping("/exam/start/{id}")
    public String startExam(@PathVariable Long id, Model model, Principal principal) {
        Exam exam = examService.getExamWithQuestionsAndChoices(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exam not found"));

        Student student = studentService.findByUsername(principal.getName());
        model.addAttribute("exam", exam);
        model.addAttribute("studentName", student.getName());
        return "exam-start";
    }

    // ✅ ส่งคำตอบ
    @PostMapping("/exam/submit/{id}")
    public String submitExam(
            @PathVariable Long id,
            @RequestParam Map<String, String> answers,
            Principal principal
    ) {
        Student student = studentService.findByUsername(principal.getName());
        Exam exam = examService.findExamWithQuestionsAndChoices(id);
        if (exam == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Exam not found");
        }


        double totalScore = 0.0;

        for (Question question : exam.getQuestions()) {
            String key = "answers[" + question.getId() + "]";
            String submittedValue = answers.get(key);

            if (submittedValue == null || submittedValue.isBlank()) continue;

            StudentAnswer studentAnswer = new StudentAnswer();
            studentAnswer.setStudent(student);
            studentAnswer.setExam(exam);
            studentAnswer.setQuestion(question);
            studentAnswer.setSubmittedAt(LocalDateTime.now());

            double questionScore = 0.0;

            if (question.getType() == QuestionType.MULTIPLE_CHOICE) {
                Long choiceId = Long.parseLong(submittedValue);
                Choice selected = choiceRepository.findById(choiceId).orElse(null);
                studentAnswer.setSelectedChoice(selected);

                Choice correct = question.getChoices().stream()
                        .filter(Choice::getIsCorrect)
                        .findFirst()
                        .orElse(null);

                if (correct != null && correct.getId().equals(choiceId)) {
                    studentAnswer.setCorrect(true);
                    questionScore = question.getFullScore();
                } else {
                    studentAnswer.setCorrect(false);
                }

            } else if (question.getType() == QuestionType.TEXT) {
                studentAnswer.setTextAnswer(submittedValue);
                studentAnswer.setCorrect(false); // ต้องตรวจภายหลัง
            }

            studentAnswer.setScore(questionScore);
            totalScore += questionScore;
            studentAnswerRepository.save(studentAnswer);
        }

        StudentExamResult result = studentExamResultService.saveResult(student.getId(), id, totalScore);
        return "redirect:/student/exam/result/" + result.getId();
    }

    // ✅ ดูคำตอบ
    @GetMapping("/answers/{examId}")
    public String viewAnswers(@PathVariable Long examId, Model model, Principal principal) {
        Optional<User> userOpt = userRepository.findByUsername(principal.getName());
        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        Student student = studentService.findByUsername(principal.getName());
        Exam exam = examService.findById(examId);
        List<StudentAnswer> answers = studentAnswerRepository.findByStudentAndExam(Optional.of(student.getUser()), exam);

        model.addAttribute("answers", answers);
        model.addAttribute("studentName", student.getName());
        return "student-answers";
    }

    // ✅ ดูผลคะแนน
    @GetMapping("/exam/result/{id}")
    public String showResult(@PathVariable Long id, Model model) {
        StudentExamResult result = studentExamResultService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Result not found"));

        model.addAttribute("result", result);
        model.addAttribute("studentName", result.getStudent().getName());
        return "results";
    }
}
