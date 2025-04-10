package com.wewe.solarproject.controller;

import com.wewe.solarproject.entity.Customer;
import com.wewe.solarproject.entity.Team;
import com.wewe.solarproject.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @GetMapping
    public Page<Team> getAllTeams(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return teamService.getAllCustomers(name, page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Team> getCustomerById(@PathVariable Long id) {
        return teamService.getCustomerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Customer createTeam(@RequestBody Customer customer) {
        return teamService.createTeam(team);
    }

    @PutMapping("/{id}")
    public Team updateTeam(@PathVariable Long id, @RequestBody Team team) {
        return teamService.updateTeam(id, team);
    }

    @DeleteMapping("/{id}")
    public void delelteCustomer(@PathVariable Long id) {
        teamService.deleteTeam(id);
    }
}

