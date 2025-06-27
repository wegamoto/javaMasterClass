package com.wewe.eduexam.service.impl;

import com.wewe.eduexam.repository.ResultRepository;
import com.wewe.eduexam.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.wewe.eduexam.model.Result;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final ResultRepository resultRepository;

    @Override
    public List<Result> getAllResults() {
        return resultRepository.findAll(); // ใช้ JPA ดึงทั้งหมด
    }
}
