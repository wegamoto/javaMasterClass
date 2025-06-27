package com.wewe.eduexam.repository;

import com.wewe.eduexam.dto.ChoiceDTO;
import com.wewe.eduexam.model.Choice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChoiceRepository extends JpaRepository<Choice, Long> {
    // ใช้ Spring Data JPA Query Method
    List<Choice> findByQuestionId(Long questionId);

    /**
     * ใช้ DTO projection เพื่อลดข้อมูลที่ไม่จำเป็น
     */
    @Query("SELECT new com.wewe.eduexam.dto.ChoiceDTO(c.id, c.content, c.isCorrect, c.question.content) " +
            "FROM Choice c " +
            "WHERE c.question.id = :questionId")
    List<ChoiceDTO> getChoicesByQuestionId(@Param("questionId") Long questionId);

    /**
     * ดึงตัวเลือกทั้งหมดจากหลาย question ids (ถ้าต้องการ preload ทั้งชุด)
     */
    @Query("SELECT c FROM Choice c WHERE c.question.id IN :questionIds")
    List<Choice> findAllByQuestionIds(@Param("questionIds") List<Long> questionIds);
}

