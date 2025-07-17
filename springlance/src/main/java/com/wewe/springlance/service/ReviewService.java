package com.wewe.springlance.service;

import com.wewe.springlance.model.Review;

import java.util.Optional;

public interface ReviewService {
    Review save(Review review);
    Optional<Review> findById(Long id);
    Review findByProjectId(Long projectId);
}
