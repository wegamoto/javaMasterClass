// implementation
package com.wewe.eduexam.service.impl;

import com.wewe.eduexam.model.*;
import com.wewe.eduexam.repository.*;
import com.wewe.eduexam.service.ExamService;
import com.wewe.eduexam.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;
    private final UserRepository userRepository;
    private final StudentAnswerRepository studentAnswerRepository;
    private final QuestionRepository questionRepository;
    private final ChoiceRepository choiceRepository;
    private final UserService userService;
    private final StudentRepository studentRepository;

    public List<Exam> findAllWithQuestions() {
        return examRepository.findAllWithQuestions();
    }

    @Autowired
    public ExamServiceImpl(ExamRepository examRepository, UserRepository userRepository,
                           StudentAnswerRepository studentAnswerRepository,
                           QuestionRepository questionRepository, ChoiceRepository choiceRepository, UserService userService, StudentRepository studentRepository) {
        this.examRepository = examRepository;
        this.userRepository = userRepository;
        this.studentAnswerRepository = studentAnswerRepository;
        this.questionRepository = questionRepository;
        this.choiceRepository = choiceRepository;
        this.userService = userService;
        this.studentRepository = studentRepository;
    }

    @Override
    public Exam createExam(Exam exam) {
        return examRepository.save(exam);
    }

    @Override
    public List<Exam> getAllExams() {
        return examRepository.findAllWithQuestions();
    }

    @Override
    public Optional<Exam> getExamById(Long id) {
        return examRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<Exam> updateExam(Long id, Exam updatedExam) {
        return examRepository.findById(id).map(exam -> {
            exam.setTitle(updatedExam.getTitle());
            exam.setDescription(updatedExam.getDescription());
            exam.setStartTime(updatedExam.getStartTime());
            exam.setEndTime(updatedExam.getEndTime());
            return examRepository.save(exam);
        });
    }

    @Override
    @Transactional
    public boolean deleteExam(Long id) {
        if (examRepository.existsById(id)) {
            examRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void saveStudentAnswers(Long examId, Map<String, String> answers, String username) {
        // ✅ ดึง User จาก username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("ไม่พบผู้ใช้งาน: " + username));

        // ✅ ดึง Student ที่ผูกกับ User
        Student student = studentRepository.findByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("ไม่พบ Student ที่ผูกกับผู้ใช้: " + username));

        // ✅ ดึง Exam
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("ไม่พบข้อสอบ ID: " + examId));

        for (Map.Entry<String, String> entry : answers.entrySet()) {
            String key = entry.getKey(); // ตัวอย่าง: answers[3]
            String answerValue = entry.getValue();

            if (answerValue == null || answerValue.isBlank()) continue;

            try {
                // แปลง key เป็น questionId
                Long questionId = Long.parseLong(key.replace("answers[", "").replace("]", ""));

                // ดึงคำถาม
                Question question = questionRepository.findById(questionId)
                        .orElseThrow(() -> new IllegalArgumentException("ไม่พบคำถาม ID: " + questionId));

                // ตรวจสอบคำถามอยู่ในข้อสอบนี้หรือไม่
                if (!question.getExam().getId().equals(examId)) {
                    throw new IllegalArgumentException("คำถามไม่ได้อยู่ในข้อสอบที่ระบุ");
                }

                StudentAnswer studentAnswer = new StudentAnswer();
                studentAnswer.setStudent(student);
                studentAnswer.setExam(exam);
                studentAnswer.setQuestion(question);
                studentAnswer.setSubmittedAt(LocalDateTime.now());

                if (question.getType() == QuestionType.MULTIPLE_CHOICE) {
                    Long choiceId = Long.parseLong(answerValue);
                    Choice selectedChoice = choiceRepository.findById(choiceId)
                            .orElseThrow(() -> new IllegalArgumentException("ไม่พบตัวเลือก ID: " + choiceId));

                    if (!selectedChoice.getQuestion().getId().equals(questionId)) {
                        throw new IllegalArgumentException("ตัวเลือกไม่ได้อยู่ในคำถามนี้");
                    }

                    studentAnswer.setSelectedChoice(selectedChoice);
                    studentAnswer.setTextAnswer(null);

                    // ตรวจสอบว่าถูกหรือไม่
                    boolean isCorrect = selectedChoice.getIsCorrect();
                    studentAnswer.setCorrect(isCorrect);

                    // กำหนดคะแนนถ้าถูก
                    studentAnswer.setScore(isCorrect ? (question.getFullScore() != null ? question.getFullScore() : 0) : 0);

                } else if (question.getType() == QuestionType.TEXT) {
                    studentAnswer.setTextAnswer(answerValue.trim());
                    studentAnswer.setSelectedChoice(null);
                    studentAnswer.setCorrect(false); // ต้องตรวจสอบภายหลังโดยครู
                    studentAnswer.setScore(0.0);
                }

                studentAnswerRepository.save(studentAnswer);

            } catch (Exception ex) {
                System.err.println("❌ เกิดข้อผิดพลาดขณะบันทึกคำตอบ: " + ex.getMessage());
                // หรือ log แล้วข้ามคำตอบนี้ไป
            }
        }
    }



    // ✅ เพิ่ม method นี้

    @Override
    public Optional<Exam> getExamWithQuestionsAndChoices(Long id) {
        return examRepository.findExamWithQuestionsAndChoices(id);
    }

    @Override
    public Exam findExamWithQuestionsAndChoices(Long examId) {
        return examRepository.findExamWithQuestionsAndChoices(examId)
                .orElseThrow(() -> new IllegalArgumentException("ไม่พบข้อสอบ id: " + examId));
    }

    @Override
    @Transactional
    public void processStudentAnswers(Long examId, Map<String, String> answers) {
        // ดึงข้อสอบ
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("ไม่พบข้อสอบ ID: " + examId));

        // ดึงผู้ใช้งาน (User)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            throw new IllegalStateException("ไม่พบผู้ใช้งาน: " + username);
        }

        // ดึง Student จาก User
        Student student = studentRepository.findByUser_Username(username)
                .orElseThrow(() -> new IllegalStateException("ไม่พบ Student ที่ผูกกับผู้ใช้: " + username));

        // สร้าง map คำถามในข้อสอบ เพื่อ lookup เร็วขึ้น
        Map<Long, Question> questionMap = exam.getQuestions().stream()
                .collect(Collectors.toMap(Question::getId, q -> q));

        for (Map.Entry<String, String> entry : answers.entrySet()) {
            String key = entry.getKey(); // เช่น "answers[3]" หรือ "3"
            String answerValue = entry.getValue();

            if (answerValue == null || answerValue.isBlank()) continue;

            try {
                Long questionId = Long.parseLong(key.replaceAll("[^0-9]", ""));

                Question question = questionMap.get(questionId);
                if (question == null) {
                    System.out.println("❌ Question ID ไม่อยู่ในข้อสอบ: " + questionId);
                    continue;
                }

                StudentAnswer studentAnswer = new StudentAnswer();
                studentAnswer.setStudent(student);
                studentAnswer.setExam(exam);
                studentAnswer.setQuestion(question);
                studentAnswer.setSubmittedAt(LocalDateTime.now());

                if (question.getType() == QuestionType.MULTIPLE_CHOICE) {
                    try {
                        Long choiceId = Long.parseLong(answerValue);
                        Choice selectedChoice = choiceRepository.findById(choiceId).orElse(null);

                        if (selectedChoice != null && selectedChoice.getQuestion().getId().equals(questionId)) {
                            studentAnswer.setSelectedChoice(selectedChoice);
                            studentAnswer.setTextAnswer(null);

                            // เช็คคำตอบถูกผิด และให้คะแนน
                            boolean isCorrect = selectedChoice.getIsCorrect();
                            studentAnswer.setCorrect(isCorrect);
                            studentAnswer.setScore(isCorrect ? (question.getFullScore() != null ? question.getFullScore() : 0) : 0);

                        } else {
                            System.out.println("❌ Choice ไม่สัมพันธ์กับคำถาม ID: " + questionId);
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Choice ID ไม่ถูกต้อง: " + answerValue);
                        continue;
                    }

                } else if (question.getType() == QuestionType.TEXT) {
                    studentAnswer.setTextAnswer(answerValue.trim());
                    studentAnswer.setSelectedChoice(null);
                    studentAnswer.setCorrect(false); // ให้ครูตรวจภายหลัง
                    studentAnswer.setScore(0.0);
                }

                // บันทึกคำตอบ
                studentAnswerRepository.save(studentAnswer);

            } catch (NumberFormatException e) {
                System.out.println("❌ Question ID ไม่ถูกต้อง: " + key);
            } catch (Exception e) {
                System.out.println("❌ เกิดข้อผิดพลาดในการประมวลผลคำตอบ: " + e.getMessage());
            }
        }
    }



    @Override
    public Exam findById(Long id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ไม่พบข้อสอบ ID: " + id));
    }

    @Transactional
    public Exam getExamWithQuestions(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Exam not found"));

        Hibernate.initialize(exam.getQuestions()); // ดึง questions ออกมาก่อน session ปิด

        return exam;
    }

}
