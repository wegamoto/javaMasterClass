package com.wewe.springlance.service.impl;

import com.wewe.springlance.model.Review;
import com.wewe.springlance.repository.ReviewRepository;
import com.wewe.springlance.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository repository;

    public ReviewServiceImpl(ReviewRepository repository) {
        this.repository = repository;
    }

    @Override
    public Review save(Review review) {
        return repository.save(review);
    }

    @Override
    public Optional<Review> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Review findByProjectId(Long projectId) {
        return repository.findByProjectId(projectId);
    }
}
