package com.wewe.eduexam.service;

// interface

import com.wewe.eduexam.controller.StudentExamController;
import com.wewe.eduexam.model.Exam;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ExamService {

    /**
     * สร้างหรือบันทึกข้อสอบใหม่
     * @param exam exam object
     * @return exam ที่ถูกบันทึก
     */
    Exam createExam(Exam exam);

    /**
     * ดึงรายการข้อสอบทั้งหมด
     * @return รายการข้อสอบ
     */
    List<Exam> getAllExams();

    /**
     * ดึงข้อสอบตาม ID
     * @param id รหัสข้อสอบ
     * @return Optional<Exam>
     */
    Optional<Exam> getExamById(Long id);

    /**
     * อัปเดตข้อสอบตาม ID
     * @param id รหัสข้อสอบ
     * @param updatedExam exam ที่จะอัปเดต
     * @return Optional<Exam> ที่ถูกอัปเดต
     */
    Optional<Exam> updateExam(Long id, Exam updatedExam);

    /**
     * ลบข้อสอบตาม ID
     * @param id รหัสข้อสอบ
     * @return true ถ้าลบสำเร็จ, false ถ้าไม่พบ
     */
    boolean deleteExam(Long id);

    void saveStudentAnswers(Long examId, Map<String, String> answers, String username);

//    // ✅ เพิ่ม method นี้
    Optional<Exam> getExamWithQuestionsAndChoices(Long id);

//    Exam saveExam(Exam exam);

    // ถ้าต้องการ method ที่คืน Exam แบบไม่ Optional (อาจโยน exception เมื่อไม่พบ)
    public Exam findExamWithQuestionsAndChoices(Long examId);

    void processStudentAnswers(Long id, Map<String, String> answers);

    Exam findById(Long examId);

    Exam getExamWithQuestions(Long examId);
}

