package com.wewe.crudapi.controllers;

import com.wewe.crudapi.entity.Investment;
import com.wewe.crudapi.services.InvestmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/investments")
public class InvestmentController {

    private final InvestmentService investmentService;

    public InvestmentController(InvestmentService investmentService) {
        this.investmentService = investmentService;
    }

    @PostMapping
    public ResponseEntity<Investment> invest(@RequestParam Long investorId, @RequestParam Long projectId, @RequestParam double amount) {
        return ResponseEntity.ok(investmentService.invest(investorId, projectId, amount));
    }

    @GetMapping("/investor/{investorId}")
    public ResponseEntity<List<Investment>> getInvestmentsByInvestor(@PathVariable Long investorId) {
        return ResponseEntity.ok(investmentService.getInvestmentsByInvestor(investorId));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Investment>> getInvestmentsByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(investmentService.getInvestmentsByProject(projectId));
    }
}

