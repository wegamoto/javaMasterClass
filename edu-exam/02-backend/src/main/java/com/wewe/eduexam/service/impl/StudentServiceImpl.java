package com.wewe.eduexam.service.impl;

import com.wewe.eduexam.model.Student;
import com.wewe.eduexam.repository.StudentRepository;
import com.wewe.eduexam.repository.UserRepository;
import com.wewe.eduexam.service.StudentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void saveStudent(Student student) {
        studentRepository.save(student);
    }

    @Override
    public Student findByUsername(String username) {
        return userRepository.findByUsername(username)
                .flatMap(user -> studentRepository.findByUser_Username(username))
                .orElseThrow(() -> new EntityNotFoundException("ไม่พบ Student ที่ผูกกับ Username: " + username));
    }
}

