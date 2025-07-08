package com.wewe.eduexam.controller;

import com.wewe.eduexam.model.Student;
import com.wewe.eduexam.model.Result;
import com.wewe.eduexam.repository.StudentRepository;
import com.wewe.eduexam.service.ResultService;
import com.wewe.eduexam.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final StudentRepository studentRepository;
    private final ResultService resultService;

    public StudentController(StudentService studentService,
                             StudentRepository studentRepository,
                             ResultService resultService) {
        this.studentService = studentService;
        this.studentRepository = studentRepository;
        this.resultService = resultService;
    }

    @GetMapping
    public String listStudents(Model model) {
        List<Student> students = studentRepository.findAll();
        model.addAttribute("students", students);
        return "student_list";
    }

    @GetMapping("/new")
    public String newStudentForm(Model model) {
        model.addAttribute("student", new Student());
        return "student_form";
    }

    @PostMapping("/save")
    public String saveStudent(@Valid @ModelAttribute("student") Student student,
                              BindingResult bindingResult,
                              Model model) {
        // Validate username or user association before save
        if (student.getUsername() == null || student.getUsername().isBlank()) {
            bindingResult.rejectValue("username", "error.student", "Username ต้องไม่ว่าง");
        }

        // Optional: ตรวจสอบว่า student ต้องผูกกับ user_id (user) ที่มีใน DB
        if (student.getUser() == null || student.getUser().getId() == null) {
            bindingResult.rejectValue("user", "error.student", "Student ต้องผูกกับ User ที่ถูกต้อง");
        }

        if (bindingResult.hasErrors()) {
            return "student_form";
        }

        try {
            studentService.saveStudent(student);
        } catch (Exception e) {
            model.addAttribute("error", "บันทึกข้อมูลล้มเหลว: " + e.getMessage());
            return "student_form";
        }

        return "redirect:/students";
    }

    @GetMapping("/edit/{id}")
    public String editStudentForm(@PathVariable Long id, Model model) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ไม่พบนักเรียน ID: " + id));
        model.addAttribute("student", student);
        return "student_form";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentRepository.deleteById(id);
        return "redirect:/students";
    }

    @GetMapping("/results")
    public String viewResults(Model model) {
        List<Result> results = resultService.getAllResults();
        model.addAttribute("results", results);
        return "results";
    }
}
