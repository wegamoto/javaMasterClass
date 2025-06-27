package com.wewe.eduexam.repository;

import com.wewe.eduexam.model.Exam;
import com.wewe.eduexam.model.Student;
import com.wewe.eduexam.model.StudentExamResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentExamResultRepository extends JpaRepository<StudentExamResult, Long> {
    Optional<StudentExamResult> findByStudentAndExam(Student student, Exam exam);

    Optional<StudentExamResult> findByStudentIdAndExamId(Long studentId, Long examId);

}
