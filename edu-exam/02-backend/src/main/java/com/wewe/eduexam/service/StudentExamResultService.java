package com.wewe.eduexam.service;

import com.wewe.eduexam.model.StudentExamResult;

import java.util.Optional;

public interface StudentExamResultService {
    StudentExamResult saveResult(Long studentId, Long examId, Integer totalScore);
    Optional<StudentExamResult> findById(Long resultId);
}
