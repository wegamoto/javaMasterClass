package com.wewe.eduexam.controller;

import com.wewe.eduexam.model.Exam;
import com.wewe.eduexam.service.ExamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
@CrossOrigin(origins = "*") // เพื่อให้ frontend เรียกได้
public class ExamController {

    @Autowired
    private ExamService examService;

    @PostMapping("/api/exams")
    public String createExam(@Valid Exam exam, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "exam-form";  // กลับไปหน้า form แสดง error
        }
        examService.createExam(exam);
        return "redirect:/exams";
    }

    @GetMapping
    public List<Exam> getAllExams() {
        return examService.getAllExams(); // ✅ ดึงทั้งหมด
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exam> getExamById(@PathVariable Long id) {
        return examService.getExamById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build()); // ✅ Optional
    }

    @DeleteMapping("/{id}")
    public void deleteExam(@PathVariable Long id) {
        examService.deleteExam(id); // ✅ ลบด้วย ID
    }
}
