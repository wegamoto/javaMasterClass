package com.wewe.eduexam.service;

import com.wewe.eduexam.dto.LoginRequest;
import com.wewe.eduexam.dto.RegisterRequest;
import com.wewe.eduexam.model.Role;
import com.wewe.eduexam.model.Student;
import com.wewe.eduexam.model.User;
import com.wewe.eduexam.repository.StudentRepository;
import com.wewe.eduexam.repository.UserRepository;
import com.wewe.eduexam.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public User register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // สร้าง User
        User user = createUserFromRequest(request);

        // ถ้าเป็น STUDENT ให้สร้าง Student และเชื่อม user ให้ครบ
        if (user.getRole() == Role.STUDENT) {
            Student student = createStudentFromRequest(request, user);
            user.setStudent(student); // <-- สำคัญมาก ต้องมี
        }

        // save user พร้อม student (เพราะ cascade = ALL)
        return userRepository.save(user);
    }

    public String login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        return jwtUtil.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRole().name())
                        .build()
        );
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public String getStudentFullName(User user) {
        String firstName = user.getFirstName();
        String lastName = user.getLastName();

        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        } else if (firstName != null) {
            return firstName;
        } else if (lastName != null) {
            return lastName;
        } else {
            return user.getUsername();
        }
    }

    // --------- Private Helpers ---------

    private User createUserFromRequest(RegisterRequest request) {
        Role role = Role.STUDENT; // default to STUDENT
        return User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();
    }

    private Student createStudentFromRequest(RegisterRequest request, User user) {
        return Student.builder()
                .studentCode(request.getStudentId())
                .name(request.getStudentName())
                .email(request.getEmail())
                .user(user) // <-- สำคัญ
                .build();
    }
}
