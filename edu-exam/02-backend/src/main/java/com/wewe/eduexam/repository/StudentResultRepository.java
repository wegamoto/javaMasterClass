package com.wewe.eduexam.repository;

import com.wewe.eduexam.model.StudentAnswer;
import com.wewe.eduexam.model.StudentResult;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentResultRepository extends JpaRepository<StudentResult, Long> {

    // ดึงผลสอบทั้งหมดของนักเรียนคนหนึ่ง
//    @EntityGraph(attributePaths = {"exam", "question", "choice"})
//    List<StudentAnswer> findByStudentId(Long studentId);

    @Query("SELECT sr FROM StudentResult sr WHERE sr.student.id = :studentId")
    List<StudentResult> findByStudentId(@Param("studentId") Long studentId);


    // ดึงผลสอบทั้งหมดของแบบทดสอบใด ๆ
    List<StudentResult> findByExamId(Long examId);

    // ดึงผลสอบของนักเรียนเฉพาะคนในแบบทดสอบเฉพาะอัน
    StudentResult findByStudentIdAndExamId(Long studentId, Long examId);

    @Query("SELECT sr FROM StudentResult sr JOIN FETCH sr.exam WHERE sr.student.id = :studentId")
    List<StudentResult> findResultsWithExamByStudentId(@Param("studentId") Long studentId);


}
