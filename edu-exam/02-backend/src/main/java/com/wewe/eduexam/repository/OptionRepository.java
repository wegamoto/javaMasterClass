package com.wewe.eduexam.repository;

import com.wewe.eduexam.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Long> {

    /**
     * ดึง option ทั้งหมดของคำถามตาม questionId
     */
    List<Option> findByQuestionId(Long questionId);

    /**
     * ดึง option หลายข้อ จากรายการ questionId
     */
    @Query("SELECT o FROM Option o WHERE o.question.id IN :questionIds")
    List<Option> findAllByQuestionIds(@Param("questionIds") List<Long> questionIds);

    /**
     * ดึงเฉพาะ option ที่ถูกต้อง ของคำถาม
     */
    @Query("SELECT o FROM Option o WHERE o.question.id = :questionId AND o.correct = true")
    List<Option> findCorrectOptions(@Param("questionId") Long questionId);

    // วิธี 2: ใช้ Query method ชื่อที่ถูกต้อง (ถ้าไม่ใช้ @Query)
    List<Option> findByQuestionIdAndCorrectTrue(Long questionId);
}
