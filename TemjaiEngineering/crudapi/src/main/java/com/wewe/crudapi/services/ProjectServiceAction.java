package com.wewe.crudapi.services;

import com.wewe.crudapi.entity.Project;
import com.wewe.crudapi.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceAction implements ProjectService {

    private ProjectRepository projectRepository;

    @Autowired
    public ProjectServiceAction(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Project save(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @Override
    public Project findById(Integer id) {
        Optional<Project> result = projectRepository.findById(id);
        Project data = null;
        if(result.isPresent()) {
            data = result.get();
        } else {
            throw new RuntimeException("ไม่พบข้อมูลของรหัส Project " + id);
        }
        return data;
    }

    @Override
    public void deleteById(Integer id) {
        projectRepository.deleteById(id);
    }
