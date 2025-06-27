package com.wewe.eduexam.controller;

import com.wewe.eduexam.model.Student;
import com.wewe.eduexam.repository.StudentRepository;
import com.wewe.eduexam.service.ResultService;
import com.wewe.eduexam.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.wewe.eduexam.model.Result;
import java.util.List;

@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ResultService resultService;

    @GetMapping("/students")
    public String listStudents(Model model) {
        List<Student> students = studentRepository.findAll();
        model.addAttribute("students", students);
        return "student_list"; // HTML Template ชื่อ student_list.html
    }

    @GetMapping("/students/new")
    public String newStudent(Model model) {
        model.addAttribute("student", new Student());
        return "student_form";
    }

    @PostMapping("/students/save")
    public String saveStudent(@ModelAttribute("student") Student student) {
        studentService.saveStudent(student); // บันทึกผ่าน service
        return "redirect:/students"; // กลับไปหน้ารายการ
    }


    @GetMapping("/students/edit/{id}")
    public String editStudent(@PathVariable Long id, Model model) {
        Student student = studentRepository.findById(id).orElseThrow();
        model.addAttribute("student", student);
        return "student_form";
    }

    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentRepository.deleteById(id);
        return "redirect:/students";
    }

    @GetMapping("/students/results")
    public String viewResults(Model model) {
        List<Result> results = resultService.getAllResults(); // ดึงผลคะแนนทั้งหมด
        model.addAttribute("results", results);
        return "results"; // 👉 ไปยังไฟล์ results.html
    }

}

