package com.wewe.eduexam.service.impl;

import com.wewe.eduexam.model.Student;
import com.wewe.eduexam.model.User;
import com.wewe.eduexam.repository.StudentRepository;
import com.wewe.eduexam.repository.UserRepository;
import com.wewe.eduexam.service.StudentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public StudentServiceImpl(StudentRepository studentRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void saveStudent(Student student) {
        if (student.getUsername() == null || student.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username ของ Student ต้องไม่ว่าง");
        }

        User user = userRepository.findByUsername(student.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("ไม่พบ User ที่มี Username: " + student.getUsername()));

        student.setUser(user); // ✅ ผูก User กับ Student ก่อนบันทึก
        studentRepository.save(student);
    }

    @Override
    public Student findByUsername(String username) {
        return userRepository.findByUsername(username)
                .flatMap(user -> studentRepository.findByUser_Username(username))
                .orElseThrow(() -> new EntityNotFoundException("ไม่พบ Student ที่ผูกกับ Username: " + username));
    }
}
