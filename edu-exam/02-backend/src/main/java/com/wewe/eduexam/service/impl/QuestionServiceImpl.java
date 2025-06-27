package com.wewe.eduexam.service.impl;

import com.wewe.eduexam.model.Exam;
import com.wewe.eduexam.model.Question;
import com.wewe.eduexam.repository.ExamRepository;
import com.wewe.eduexam.repository.QuestionRepository;
import com.wewe.eduexam.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ExamRepository examRepository;

    @Override
    public Question createQuestion(Long examId, Question question) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        question.setExam(exam);
        if (question.getChoices() != null) {
            question.getChoices().forEach(choice -> choice.setQuestion(question));
        }
        return questionRepository.save(question);
    }

    @Override
    public List<Question> getQuestionsByExamId(Long examId) {
        return questionRepository.findByExamId(examId);
    }

    public List<Question> findAllWithExam() {
        return questionRepository.findAllWithExam(); // ใช้ JOIN FETCH
    }

    public Question findByIdWithExam(Long id) {
        return questionRepository.findByIdWithExam(id)
                .orElseThrow(() -> new IllegalArgumentException("ไม่พบคำถาม id: " + id));
    }

    public Question findByIdWithChoices(Long id) {
        return questionRepository.findByIdWithChoices(id)
                .orElseThrow(() -> new IllegalArgumentException("ไม่พบคำถาม id: " + id));
    }

    public void saveQuestion(Question question) {
        questionRepository.save(question);
    }

}

