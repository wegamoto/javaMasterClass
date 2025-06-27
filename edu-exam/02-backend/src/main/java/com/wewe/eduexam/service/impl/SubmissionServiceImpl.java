package com.wewe.eduexam.service.impl;

import com.wewe.eduexam.model.*;
import com.wewe.eduexam.repository.*;
import com.wewe.eduexam.service.SubmissionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final ExamRepository examRepository;
    private final ChoiceRepository choiceRepository;

    public SubmissionServiceImpl(SubmissionRepository submissionRepository,
                                 ExamRepository examRepository,
                                 ChoiceRepository choiceRepository) {
        this.submissionRepository = submissionRepository;
        this.examRepository = examRepository;
        this.choiceRepository = choiceRepository;
    }

    /**
     * บันทึกการส่งข้อสอบและคำนวณคะแนน
     *
     * @param examId      รหัสข้อสอบ
     * @param submission  ข้อมูลการส่งข้อสอบ
     * @return ข้อมูลการส่งข้อสอบที่บันทึกแล้ว
     */
    @Override
    public Submission submitExam(Long examId, Submission submission) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        submission.setExam(exam);

        double score = 0.0;

        if (submission.getAnswers() != null) {
            for (Answer answer : submission.getAnswers()) {
                Long choiceId = Optional.ofNullable(answer.getSelectedChoice())
                        .map(Choice::getId)
                        .orElseThrow(() -> new RuntimeException("Selected choice is null"));

                Choice selected = choiceRepository.findById(choiceId)
                        .orElseThrow(() -> new RuntimeException("Choice not found"));

                // ตรวจสอบ question ที่ดึงจาก choice ว่าไม่เป็น null
                Question question = selected.getQuestion();
                if (question == null) {
                    throw new RuntimeException("Question not found for the selected choice");
                }
                answer.setQuestion(question);
                answer.setSelectedChoice(selected);
                answer.setSubmission(submission);

                if (selected.getIsCorrect()) {
                    score++;
                }
            }
        }

        submission.setScore(score);

        return submissionRepository.save(submission);
    }

    /**
     * ดึงรายงานการส่งข้อสอบของนักเรียนรายคน
     *
     * @param studentName ชื่อนักเรียน
     * @return รายการ Submission
     */
    @Override
    public List<Submission> getSubmissionsByStudent(String studentName) {
        return submissionRepository.findByStudentName(studentName);
    }

    /**
     * ดึงรายงานการส่งข้อสอบของนักเรียนคนหนึ่งในข้อสอบใดข้อสอบหนึ่ง
     *
     * @param studentName ชื่อนักเรียน
     * @param examId      รหัสข้อสอบ
     * @return รายการ Submission
     */
    @Override
    public List<Submission> getSubmissionsByStudentAndExam(String studentName, Long examId) {
        return submissionRepository.findByStudentNameAndExamId(studentName, examId);
    }

    @Override
    public void processStudentAnswers(Long examId, Map<String, String> answers) {
        // ✅ Logic การตรวจคำตอบ หรือบันทึก
        System.out.println("ตรวจข้อสอบ ID: " + examId);
    }
}
