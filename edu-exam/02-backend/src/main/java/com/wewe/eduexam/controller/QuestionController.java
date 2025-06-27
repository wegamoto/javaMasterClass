package com.wewe.eduexam.controller;

import com.wewe.eduexam.model.Question;
import com.wewe.eduexam.service.impl.QuestionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams/{examId}/questions")
@CrossOrigin(origins = "*")
public class QuestionController {

    @Autowired
    private QuestionServiceImpl questionService;

    @PostMapping
    public Question createQuestion(@PathVariable Long examId, @RequestBody Question question) {
        return questionService.createQuestion(examId, question);
    }

    @GetMapping
    public List<Question> getQuestions(@PathVariable Long examId) {
        return questionService.getQuestionsByExamId(examId);
    }
}

