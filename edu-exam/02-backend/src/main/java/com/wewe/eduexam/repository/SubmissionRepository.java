package com.wewe.eduexam.repository;

import com.wewe.eduexam.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByStudentName(String studentName);
    List<Submission> findByStudentNameAndExamId(String studentName, Long examId);

}

