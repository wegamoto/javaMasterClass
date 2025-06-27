package com.wewe.eduexam.service;

import com.wewe.eduexam.model.Student;
import com.wewe.eduexam.model.User;
import com.wewe.eduexam.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface StudentService {

    void saveStudent(Student student);
    Student findByUsername(String username);

}

