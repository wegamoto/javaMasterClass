package com.wewe.eduexam.exception;

public class ExamNotFoundException extends RuntimeException {

    public ExamNotFoundException(Long id) {
        super("Exam not found with ID: " + id);
    }

    public ExamNotFoundException(String message) {
        super(message);
    }
}
