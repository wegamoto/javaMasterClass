package com.wewe.eduexam.service;

import com.wewe.eduexam.dto.StudentResultDTO;
import com.wewe.eduexam.model.StudentResult;
import com.wewe.eduexam.model.User;
import com.wewe.eduexam.repository.StudentResultRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentResultService {

    private final StudentResultRepository studentResultRepository;

    public StudentResultService(StudentResultRepository studentResultRepository) {
        this.studentResultRepository = studentResultRepository;
    }

    public List<StudentResultDTO> getAllResults() {
        List<StudentResult> results = studentResultRepository.findAll();
        return results.stream().map(this::toDTO).toList();
    }

    @Transactional
    public List<StudentResultDTO> getResultsForStudent(Long studentId) {
        List<StudentResult> studentResults = studentResultRepository.findByStudentId(studentId);

        return studentResults.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private StudentResultDTO toDTO(StudentResult sr) {
        if (sr == null) return null;

        return StudentResultDTO.builder()
                .studentName(sr.getStudent() != null ? sr.getStudent().getStudentFullName() : "ไม่ระบุชื่อ")
                .examTitle(sr.getExam() != null ? sr.getExam().getTitle() : "ไม่ระบุชื่อข้อสอบ")
                .questionText(sr.getQuestion() != null ? sr.getQuestion().getContent() : null)
                .choiceText(sr.getChoice() != null ? sr.getChoice().getContent() : null)
                .textAnswer(sr.getTextAnswer())
                .score(sr.getScore() != null ? sr.getScore() : 0.0)
                .maxScore(sr.getMaxScore() != null ? sr.getMaxScore() : 0.0)
                .submittedAt(sr.getSubmittedAt())
                .build();
    }

    // ตัวอย่างฟังก์ชันสำหรับรวมชื่อเต็ม
    private String getStudentFullName(User student) {
        return student.getFirstName() + " " + student.getLastName();
    }

    @Transactional
    public List<StudentResult> getResultsWithExam(Long studentId) {
        List<StudentResult> results = studentResultRepository.findResultsWithExamByStudentId(studentId);
        results.forEach(r -> r.getExam().getTitle()); // initialize within session
        return results;
    }
}

