package com.wewe.springlance.service.impl;

import com.wewe.springlance.model.ProjectRequest;
import com.wewe.springlance.model.User;
import com.wewe.springlance.repository.ProjectRequestRepository;
import com.wewe.springlance.service.ProjectRequestService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectRequestServiceImpl implements ProjectRequestService {

    private final ProjectRequestRepository projectRequestRepository;

    public ProjectRequestServiceImpl(ProjectRequestRepository projectRequestRepository) {
        this.projectRequestRepository = projectRequestRepository;
    }

    @Override
    public ProjectRequest save(ProjectRequest projectRequest) {
        return projectRequestRepository.save(projectRequest);
    }

    @Override
    public Optional<ProjectRequest> findById(Long id) {
        return projectRequestRepository.findById(id);
    }

    @Override
    public List<ProjectRequest> findAll() {
        return projectRequestRepository.findAll();
    }

    @Override
    public List<ProjectRequest> findByClientId(Long clientId) {
        return projectRequestRepository.findByClientId(clientId);
    }

    @Override
    public List<ProjectRequest> findByFreelancerId(Long freelancerId) {
        return projectRequestRepository.findByFreelancerId(freelancerId);
    }

    @Override
    public void delete(Long id) {
        projectRequestRepository.deleteById(id);
    }

    @Override
    public List<ProjectRequest> findByClient(Optional<User> user) {
        return projectRequestRepository.findByClient(user); // ใช้ client แทน owner ตามที่คุณปรับใน model
    }
}
