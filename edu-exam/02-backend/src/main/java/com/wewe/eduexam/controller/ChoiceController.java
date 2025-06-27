package com.wewe.eduexam.controller;

import com.wewe.eduexam.dto.ChoiceDTO;
import com.wewe.eduexam.model.Choice;
import com.wewe.eduexam.model.Question;
import com.wewe.eduexam.repository.ChoiceRepository;
import com.wewe.eduexam.repository.QuestionRepository;
import com.wewe.eduexam.service.ChoiceService;
import com.wewe.eduexam.service.impl.QuestionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class ChoiceController {

    private final ChoiceService choiceService;
    private final ChoiceRepository choiceRepository;
    private final QuestionServiceImpl questionService;
    private final QuestionRepository questionRepository;

    @Autowired
    public ChoiceController(ChoiceService choiceService, ChoiceRepository choiceRepository, QuestionServiceImpl questionService, QuestionRepository questionRepository) {
        this.choiceService = choiceService;
        this.choiceRepository = choiceRepository;
        this.questionService = questionService;
        this.questionRepository = questionRepository;
    }

    @GetMapping("/question/{questionId}")
    public String getChoicesByQuestion(@PathVariable Long questionId, Model model) {
        List<ChoiceDTO> choices = choiceService.getChoicesByQuestionId(questionId);
        model.addAttribute("choices", choices);
        model.addAttribute("questionId", questionId);
        return "choice_list"; // แก้ให้ตรงกับชื่อไฟล์ HTML
    }

//    @GetMapping("/choices/question/{questionId}")
//    public String listChoicesByQuestion(@PathVariable Long questionId, Model model) {
//        Question question = questionRepository.findById(questionId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid questionId: " + questionId));
//        List<ChoiceDTO> choices = choiceService.getChoicesByQuestionId(questionId);
//        model.addAttribute("choices", choices);
//        model.addAttribute("questionId", questionId);
//        return "choice_list"; // ชื่อ HTML template ที่คุณใช้แสดงตัวเลือก
//    }

    @GetMapping("/choices/new")
    public String showCreateForm(@RequestParam("questionId") Long questionId, Model model) {
        if (questionId == null || questionId <= 0) {
            // กลับหน้า error หรือ redirect ไปยังหน้า list ของ questions
            return "redirect:/questions"; // หรือ render หน้า error สวย ๆ
        }

        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if (optionalQuestion.isEmpty()) {
            return "redirect:/questions";
        }

        Choice choice = new Choice();
        choice.setQuestion(optionalQuestion.get());
        model.addAttribute("choice", choice);
        model.addAttribute("questions", questionRepository.findAll());

        return "choice_form";
    }

    @GetMapping("/new")
    public String createChoiceForm(@RequestParam("questionId") Long questionId, Model model) {
        Choice choice = new Choice();
        choice.setQuestion(new Question(questionId)); // สำหรับ pre-select คำถาม
        model.addAttribute("choice", choice);
        model.addAttribute("questions", questionRepository.findAll()); // เพื่อใช้ใน dropdown
        return "choice_form";
    }

    @GetMapping("/choices/edit/{id}")
    public String editChoiceForm(@PathVariable Long id, Model model) {
        Choice choice = choiceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid choice ID: " + id));

        model.addAttribute("choice", choice);
        model.addAttribute("questions", questionRepository.findAll());
        return "choice_form";
    }

    @GetMapping("/choices/delete/{id}")
    public String deleteChoice(@PathVariable Long id) {
        choiceRepository.deleteById(id);
        return "redirect:/questions";
    }

    @PostMapping("/choices/save")
    public String saveChoice(@ModelAttribute Choice choice, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("questions", questionRepository.findAll());
            return "choice_form"; // ชื่อ template HTML ที่ใช้
        }

        choiceRepository.save(choice);
        return "redirect:/questions"; // หรือ redirect ไปยังหน้าที่คุณต้องการ
    }

    @GetMapping("/choices/question/{questionId}")
    public String viewChoices(@PathVariable Long questionId, Model model) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("ไม่พบคำถามที่ id: " + questionId));
        List<Choice> choices = choiceRepository.findByQuestionId(questionId);
        model.addAttribute("question", question);
        model.addAttribute("choices", choices);
        return "choice_list"; // ตรงกับชื่อ template HTML
    }

}

