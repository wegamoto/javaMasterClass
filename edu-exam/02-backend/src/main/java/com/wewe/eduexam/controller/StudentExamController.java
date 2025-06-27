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
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/student")
public class StudentExamController {

    private final ExamRepository examRepository;

    private final ExamService examService;
    private final UserRepository userService;
    private final StudentAnswerRepository studentAnswerRepository;
    private final StudentService studentService;
    private final ChoiceRepository choiceRepository;
    private final StudentExamResultService studentExamResultService;
    private final StudentExamResultRepository studentExamResultRepository;

    public StudentExamController(ExamRepository examRepository, ExamService examService,
                                 UserRepository userService, StudentAnswerRepository studentAnswerRepository,
                                 StudentService studentService, ChoiceRepository choiceRepository,
                                 StudentExamResultService studentExamResultService,
                                 StudentExamResultRepository studentExamResultRepository) {
        this.examRepository = examRepository;
        this.examService = examService;
        this.userService = userService;
        this.studentAnswerRepository = studentAnswerRepository;
        this.studentService = studentService;
        this.choiceRepository = choiceRepository;
        this.studentExamResultService = studentExamResultService;
        this.studentExamResultRepository = studentExamResultRepository;
    }

    @GetMapping("/exams")
    public String showAvailableExams(Model model) {
        List<com.wewe.eduexam.model.Exam> exams = examRepository.findAll(); // ✅ ดึงจากฐานข้อมูล
        model.addAttribute("exams", exams);
        return "student_exams"; // ชื่อ template
    }

    // คุณอาจจะมี method เริ่มทำข้อสอบตาม path นี้
    @GetMapping("/exam/start/{id}")
    public String startExam(@PathVariable Long id, Model model) {
        // ✅ ใช้ method ที่ JOIN FETCH questions และ choices
        Optional<Exam> optionalExam = examService.getExamWithQuestionsAndChoices(id);

        if (optionalExam.isEmpty()) {
            return "redirect:/error/404"; // ✅ ไม่พบข้อสอบ
        }

        Exam exam = optionalExam.get();

        // ✅ ตรวจสอบว่า choices ถูกโหลด
        exam.getQuestions().forEach(q -> {
            System.out.println("Question: " + q.getContent());
            System.out.println("Choices: " + (q.getChoices() != null ? q.getChoices().size() : "null"));
        });

        model.addAttribute("exam", exam);
        return "exam-start"; // ✅ ชื่อไฟล์ HTML
    }

    @PostMapping("/exam/submit/{id}")
    public String submitExam(
            @PathVariable("id") Long examId,
            @RequestParam Map<String, String> answers,
            Principal principal
    ) {
        String username = principal.getName();
        Student student = studentService.findByUsername(username);

        Exam exam = examService.findExamWithQuestionsAndChoices(examId);

        int totalScore = 0;

        for (Question question : exam.getQuestions()) {
            String key = "answers[" + question.getId() + "]";
            String value = answers.get(key);

            if (value == null || value.isBlank()) {
                continue; // ✅ ข้ามถ้ายังไม่ได้ตอบ
            }

            StudentAnswer studentAnswer = new StudentAnswer();
            studentAnswer.setStudent(student);
            studentAnswer.setExam(exam); // ✅ ถ้ามีฟิลด์ exam
            studentAnswer.setQuestion(question);
            studentAnswer.setSubmittedAt(LocalDateTime.now());

            Double questionScore = 0.0;

            if (question.getType() == QuestionType.MULTIPLE_CHOICE) {
                Long selectedChoiceId = Long.parseLong(value);
                Choice selectedChoice = choiceRepository.findById(selectedChoiceId)
                        .orElse(null);
                studentAnswer.setSelectedChoice(selectedChoice);

                Choice correctChoice = question.getChoices().stream()
                        .filter(Choice::getIsCorrect)
                        .findFirst()
                        .orElse(null);

                if (correctChoice != null && correctChoice.getId().equals(selectedChoiceId)) {
                    studentAnswer.setCorrect(true);
                    questionScore = question.getFullScore(); // ✅ ตรวจจากคะแนนของคำถาม
                } else {
                    studentAnswer.setCorrect(false);
                }

            } else if (question.getType() == QuestionType.TEXT) {
                studentAnswer.setTextAnswer(value);
                studentAnswer.setCorrect(false); // ✅ รอตรวจทีหลัง
            }

            studentAnswer.setScore(questionScore);
            totalScore += questionScore;

            studentAnswerRepository.save(studentAnswer);
        }

        // ✅ บันทึกคะแนนรวม
        //studentExamResultService.saveResult(student.getId(), examId, totalScore);
        StudentExamResult result = studentExamResultService.saveResult(student.getId(), examId, totalScore);

        return "redirect:/student/exam/result/" + result.getId(); // ✅ ใช้ ID ที่แน่ใจว่าถูกต้อง
    }

    @GetMapping("/answers/{examId}")
    public String viewAnswers(@PathVariable Long examId, Model model, Principal principal) {
        Optional<User> student = userService.findByUsername(principal.getName());
        Exam exam = examService.findById(examId);

        List<StudentAnswer> answers = studentAnswerRepository.findByStudentAndExam(student, exam);

        model.addAttribute("answers", answers);
        return "student-answers"; // หน้าสำหรับแสดงคำตอบ
    }

    @GetMapping("/exam/result/{id}")
    public String showResult(@PathVariable("id") Long resultId, Model model) {
        StudentExamResult result = studentExamResultService.findById(resultId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Result not found"));

        model.addAttribute("result", result);
        return "results"; // ชื่อ view ที่คุณต้องมี
    }

}


