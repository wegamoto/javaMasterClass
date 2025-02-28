package com.wewe.crudapi.services;

import com.wewe.crudapi.entity.Project;
import com.wewe.crudapi.entity.User;

import java.util.List;

public interface ProjectService {

    Project save(Project project);

    List<Project> findAll();

    Project findById(Integer id);

    void deleteById(Integer id);

     List<Project> getAllProjects() {
        return null;
    }

     Project getProjectById(Long id) {
        return null;
    }

     Project createProject(Project project) {
        return null;
    }

     Project updateProject(Long id, Project project) {
        return null;
    }

    deleteProject(Long id) {

    }
}
