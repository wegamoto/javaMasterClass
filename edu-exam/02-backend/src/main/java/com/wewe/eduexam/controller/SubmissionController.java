package com.wewe.eduexam.controller;

import com.wewe.eduexam.model.Submission;
import com.wewe.eduexam.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams/{examId}/submissions")
@CrossOrigin(origins = "*")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    // ✅ ส่งคำตอบข้อสอบ
    @PostMapping
    public Submission submitExam(@PathVariable Long examId, @RequestBody Submission submission) {
        return submissionService.submitExam(examId, submission);
    }

    // ✅ เรียกดูคำตอบของนักเรียนทั้งหมดในข้อสอบ
    @GetMapping("/student/{studentName}")
    public List<Submission> getSubmissionsByStudent(
            @PathVariable Long examId,
            @PathVariable String studentName) {
        return submissionService.getSubmissionsByStudent(studentName);
    }

    // ✅ (Optional) หากต้องการใช้ path แบบรวม exam อีกที
    @GetMapping("/student/{studentName}/exam/{examRefId}")
    public List<Submission> getSubmissionsByStudentAndExam(
            @PathVariable String studentName,
            @PathVariable("examRefId") Long examRefId) {
        return submissionService.getSubmissionsByStudentAndExam(studentName, examRefId);
    }
}
