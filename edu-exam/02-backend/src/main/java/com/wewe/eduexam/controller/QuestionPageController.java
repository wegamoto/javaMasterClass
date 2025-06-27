package com.wewe.eduexam.controller;

import com.wewe.eduexam.model.Exam;
import com.wewe.eduexam.model.Question;
import com.wewe.eduexam.repository.ExamRepository;
import com.wewe.eduexam.service.ExamService;
import com.wewe.eduexam.service.impl.QuestionServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/questions")
public class QuestionPageController {

    private final ExamService examService;
    private final QuestionServiceImpl questionService;
    private final ExamRepository examRepository;

    public QuestionPageController(ExamService examService,
                                  QuestionServiceImpl questionService,
                                  ExamRepository examRepository) {
        this.examService = examService;
        this.questionService = questionService;
        this.examRepository = examRepository;
    }

    // ✅ GET: แสดงรายการคำถามทั้งหมด
    @GetMapping
    public String listQuestions(Model model) {
        List<Question> questions = questionService.findAllWithExam(); // JOIN FETCH exam
        model.addAttribute("questions", questions);
        return "questions";
    }

    // ✅ GET: แสดงฟอร์มสร้างคำถามใหม่
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("question", new Question());
        List<Exam> exams = examRepository.findAll();
        model.addAttribute("exams", exams != null ? exams : new ArrayList<>());
        return "question_form";
    }

    // ✅ POST: บันทึกคำถาม
    @PostMapping("/save")
    public String saveQuestion(@ModelAttribute("question") Question question) {
        questionService.saveQuestion(question); // call service
        return "redirect:/questions";
    }

    // ✅ GET: แสดงฟอร์มแก้ไขคำถาม
    @GetMapping("/edit/{id}")
    public String editQuestion(@PathVariable("id") Long id, Model model) {
        Question question = questionService.findByIdWithExam(id); // JOIN FETCH exam
        List<Exam> exams = examRepository.findAll();
        model.addAttribute("question", question);
        model.addAttribute("exams", exams);
        return "question_form";
    }

//     ❗ Optional: ดู choices ของ question
@GetMapping("/choices/question/{questionId}")
public String viewChoices(@PathVariable Long questionId, Model model) {
    Question question = questionService.findByIdWithChoices(questionId); // ใช้ JOIN FETCH
    if (question == null) {
        return "redirect:/error/404"; // หรือสร้าง error.html
    }
    model.addAttribute("question", question);
    model.addAttribute("choices", question.getChoices());
    return "choices"; // ไปยังไฟล์ choices.html
}

}
