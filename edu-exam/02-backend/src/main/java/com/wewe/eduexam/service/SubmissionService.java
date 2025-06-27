package com.wewe.eduexam.service;

import com.wewe.eduexam.model.Submission;
import java.util.List;

public interface SubmissionService {

    /**
     * บันทึกคำตอบของนักเรียนสำหรับข้อสอบที่ระบุ
     *
     * @param examId     รหัสข้อสอบ
     * @param submission ข้อมูลคำตอบของนักเรียน
     * @return Submission ที่ถูกบันทึก
     */
    Submission submitExam(Long examId, Submission submission);

    /**
     * ดึงรายการคำตอบของนักเรียนทั้งหมด (ในทุกข้อสอบ)
     *
     * @param studentName ชื่อผู้ใช้นักเรียน
     * @return รายการ Submission
     */
    List<Submission> getSubmissionsByStudent(String studentName);

    /**
     * ดึงรายการคำตอบของนักเรียนในข้อสอบที่ระบุ
     *
     * @param studentName ชื่อผู้ใช้นักเรียน
     * @param examId      รหัสข้อสอบ
     * @return รายการ Submission
     */
    List<Submission> getSubmissionsByStudentAndExam(String studentName, Long examId);

    void processStudentAnswers(Long examId, java.util.Map<String, String> answers);
}

