package com.wewe.eduexam.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wewe.eduexam.model.Result;

public interface ResultRepository extends JpaRepository<Result, Long> {
}

