package com.wewe.springlance.service;

import com.wewe.springlance.model.ProjectRequest;
import com.wewe.springlance.model.User;

import java.util.List;
import java.util.Optional;

public interface ProjectRequestService {
    ProjectRequest save(ProjectRequest projectRequest);
    Optional<ProjectRequest> findById(Long id);
    List<ProjectRequest> findAll();
    List<ProjectRequest> findByClientId(Long clientId);
    List<ProjectRequest> findByFreelancerId(Long freelancerId);
    void delete(Long id);
    List<ProjectRequest> findByClient(Optional<User> user);
}
