package com.wewe.eduexam.controller;

import com.wewe.eduexam.model.Exam;
import com.wewe.eduexam.repository.ExamRepository;
import com.wewe.eduexam.service.ExamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class ExamPageController {

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamRepository examRepository;

    @GetMapping("/exams")
    public String showExamPage(Model model) {
        List<Exam> exams = examRepository.findAllWithQuestions();
        model.addAttribute("exams", exams);
        return "exams"; // Thymeleaf page: templates/exams.html
    }

    @GetMapping("/exams/new")
    public String showCreateExamForm(Model model) {
        model.addAttribute("exam", new Exam());
        return "exam-form"; // จะเรียกไฟล์ exam-form.html
    }

    @PostMapping("/exams")
    public String createExam(
            @Valid Exam exam,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            // ถ้ามี validation error ให้แสดงฟอร์มเดิม พร้อม error message
            return "exam-form";
        }

        examService.createExam(exam);

        // บันทึกสำเร็จ redirect ไปหน้ารายการข้อสอบ
        return "redirect:/exams";
    }

    // ✅ FIX: Show edit form for a given exam ID
    @GetMapping("/exams/edit/{id}")
    public String showEditExamForm(@PathVariable("id") Long id, Model model) {
        Optional<Exam> exam = examRepository.findById(id);

        if (exam.isEmpty()) {
            return "redirect:/exams?error=ExamNotFound";
        }

        model.addAttribute("exam", exam.get());
        return "exam-form"; // reuse same form for editing
    }

    // ✅ FIX: Handle the submission of the edited form
    @PostMapping("/exams/update")
    public String updateExam(
            @Valid @ModelAttribute("exam") Exam exam,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("exam", exam); // ensure exam data is passed back to form
            return "exam-form"; // show the same form with error messages
        }

        // Optional: Check if exam exists before updating
        if (!examRepository.existsById(exam.getId())) {
            return "redirect:/exams?error=ExamNotFound";
        }

        examService.updateExam(exam.getId(),exam);
        return "redirect:/exams";
    }

}

