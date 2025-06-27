package com.wewe.eduexam.service;

import com.wewe.eduexam.model.Question;

import java.util.List;

public interface QuestionService {
    Question createQuestion(Long examId, Question question);
    List<Question> getQuestionsByExamId(Long examId);
    List<Question> findAllWithExam();
}
