package com.wewe.springlance.repository;

import com.wewe.springlance.model.Review;
import com.wewe.springlance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Review findByProjectId(Long projectId);

    // ✅ คำนวณคะแนนเฉลี่ยของฟรีแลนซ์
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.freelancer = :user")
    Double averageRatingByFreelancer(@Param("user") User user);
}
