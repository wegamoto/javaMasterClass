package com.wewe.eduexam.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExamNotFoundException.class)
    public ResponseEntity<String> handleExamNotFound(ExamNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // เพิ่ม Exception อื่นๆ ได้ที่นี่
}

