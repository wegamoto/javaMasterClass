package com.wewe.eduexam.repository;

import com.wewe.eduexam.dto.StudentAnswerDto;
import com.wewe.eduexam.model.Student;
import com.wewe.eduexam.model.StudentAnswer;
import com.wewe.eduexam.model.User;
import com.wewe.eduexam.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {

    // ดึงคำตอบทั้งหมดของผู้เรียนในข้อสอบนั้น ๆ
    List<StudentAnswer> findByStudentAndExam(Optional<User> student, Exam exam);

    List<StudentAnswer> findByStudentId(Long studentId);

    List<StudentAnswer> findByStudent(Student student);

    // ดึงคำตอบทั้งหมดของข้อสอบ
    List<StudentAnswer> findByExam(Exam exam);

    @Query("SELECT new com.wewe.eduexam.dto.StudentAnswerDto(sa.id, sa.exam.title, sa.score) " +
            "FROM StudentAnswer sa WHERE sa.student.id = :studentId")
    List<StudentAnswerDto> findDtoByStudentId(@Param("studentId") Long studentId);

}

