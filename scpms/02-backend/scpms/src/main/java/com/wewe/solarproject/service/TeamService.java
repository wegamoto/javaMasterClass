package com.wewe.solarproject.service;

import com.wewe.solarproject.entity.Team;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface TeamService {
    Page<Team> getAllTeam(String name, int page, int size);
    Optional<Team> getTeamById(Long id);
    Team createTeam(Team team);
    Team updateTeam(Long id, Team Team);
    void deleteTeam(Long id);
}

