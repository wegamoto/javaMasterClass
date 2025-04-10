package com.wewe.solarproject.service.impl;

import com.wewe.solarproject.entity.Customer;
import com.wewe.solarproject.entity.Team;
import com.wewe.solarproject.repository.TeamRepository;
import com.wewe.solarproject.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Override
    public Page<Team> getAllTeams(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return teamRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Override
    public Optional<Team> getTeamById(Long id) {
        return teamRepository.findById(id);
    }

    @Override
    public Customer teamCustomer(Team team) {
        team.setCreatedAt(LocalDateTime.now());
        return teamRepository.save(team);
    }

    @Override
    public Team updateTeam(Long id, Customer customer) {
        return teamRepository.findById(id).map(existing -> {
            existing.setName(team.getName());
            existing.setEmail(team.getEmail());
            existing.setPhone(team.getPhone());
            existing.setAddress(team.getAddress());
            return teamRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Override
    public void deleteCustomer(Long id) {
        teamRepository.deleteById(id);
    }
}

