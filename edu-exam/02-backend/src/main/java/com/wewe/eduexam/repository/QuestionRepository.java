package com.wewe.eduexam.repository;

import com.wewe.eduexam.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByExamId(Long examId);

    @Query("SELECT q FROM Question q JOIN FETCH q.exam")
    List<Question> findAllWithExam();

    @Query("SELECT q FROM Question q JOIN FETCH q.exam WHERE q.id = :id")
    Optional<Question> findByIdWithExam(@Param("id") Long id);

    @Query("SELECT q FROM Question q LEFT JOIN FETCH q.choices WHERE q.id = :id")
    Optional<Question> findByIdWithChoices(@Param("id") Long id);
}

