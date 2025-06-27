package com.wewe.eduexam.service.impl;

import com.wewe.eduexam.model.Exam;
import com.wewe.eduexam.model.Student;
import com.wewe.eduexam.model.StudentExamResult;
import com.wewe.eduexam.repository.ExamRepository;
import com.wewe.eduexam.repository.StudentExamResultRepository;
import com.wewe.eduexam.repository.StudentRepository;
import com.wewe.eduexam.service.StudentExamResultService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentExamResultServiceImpl implements StudentExamResultService {

    private final StudentExamResultRepository resultRepository;
    private final StudentRepository studentRepository;
    private final ExamRepository examRepository;

    public StudentExamResultServiceImpl(
            StudentExamResultRepository resultRepository,
            StudentRepository studentRepository,
            ExamRepository examRepository
    ) {
        this.resultRepository = resultRepository;
        this.studentRepository = studentRepository;
        this.examRepository = examRepository;
    }

    @Override
    public StudentExamResult saveResult(Long studentId, Long examId, Integer totalScore) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("ไม่พบ Student ID: " + studentId));

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("ไม่พบ Exam ID: " + examId));

        StudentExamResult result = resultRepository.findByStudentAndExam(student, exam)
                .map(existing -> {
                    existing.setTotalScore(totalScore);
                    return existing;
                })
                .orElseGet(() -> StudentExamResult.builder()
                        .student(student)
                        .exam(exam)
                        .totalScore(totalScore)
                        .build());

        return resultRepository.save(result);
    }

    @Override
    public Optional<StudentExamResult> findById(Long id) {
        return resultRepository.findById(id);
    }
}
