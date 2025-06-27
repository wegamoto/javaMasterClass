package com.wewe.eduexam.repository;

import com.wewe.eduexam.model.Exam;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam, Long> {

//     แบบปกติ ดึง Exam อย่างเดียว
     List<Exam> findAll();

    // แบบดึง Exam พร้อม questions ด้วย fetch join
    @Query("SELECT e FROM Exam e LEFT JOIN FETCH e.questions")
    List<Exam> findAllWithQuestions();

    @Query("SELECT e FROM Exam e LEFT JOIN FETCH e.questions WHERE e.id = :id")
    Optional<Exam> findByIdWithQuestions(@Param("id") Long id);


    @Query("SELECT DISTINCT e FROM Exam e " +
            "LEFT JOIN FETCH e.questions q " +
            "LEFT JOIN FETCH q.choices " +
            "WHERE e.id = :id")
    Optional<Exam> findExamWithQuestionsAndChoices(@Param("id") Long id);

    @EntityGraph(attributePaths = {"questions", "questions.options", "questions.choices"})
    Optional<Exam> findWithQuestionsAndOptionsById(Long id);

}

