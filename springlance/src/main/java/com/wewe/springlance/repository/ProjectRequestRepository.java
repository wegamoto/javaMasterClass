package com.wewe.springlance.repository;

import com.wewe.springlance.model.ProjectRequest;
import com.wewe.springlance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRequestRepository extends JpaRepository<ProjectRequest, Long> {
    List<ProjectRequest> findByClientId(Long clientId);

    List<ProjectRequest> findByFreelancerId(Long freelancerId);

    int countByClient(Optional<User> client);

    List<ProjectRequest> findAllByOrderByCreatedAtDesc();

    List<ProjectRequest> findByClient(Optional<User> client);

    @Query("SELECT p FROM ProjectRequest p WHERE p.client = :user OR p.freelancer = :user")
    List<ProjectRequest> findByUser(@Param("user") User user);
}
