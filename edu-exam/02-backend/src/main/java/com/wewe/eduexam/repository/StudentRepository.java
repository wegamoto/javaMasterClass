package com.wewe.eduexam.repository;

import com.wewe.eduexam.model.Student;
import com.wewe.eduexam.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUser(User user); // ✅ เพิ่มเมธอดนี้

    Optional<Student> findByUser_Username(String username);
}
