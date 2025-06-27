package com.servix.maintenance.controller;

import com.servix.maintenance.model.WorkRequest;
import com.servix.maintenance.model.WorkRequestStatus;
import com.servix.maintenance.repository.WorkRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/work-requests")
@RequiredArgsConstructor
public class WorkRequestController {

    private final WorkRequestRepository workRequestRepository;

    @PostMapping
    public WorkRequest createRequest(@RequestBody WorkRequest request) {
        request.setRequestDate(LocalDateTime.now());
        request.setStatus(WorkRequestStatus.NEW);
        return workRequestRepository.save(request);
    }

    @GetMapping
    public List<WorkRequest> getAllRequests() {
        return workRequestRepository.findAll();
    }

    @GetMapping("/{id}")
    public WorkRequest getById(@PathVariable Long id) {
        return workRequestRepository.findById(id).orElseThrow();
    }

    @PutMapping("/{id}/status")
    public WorkRequest updateStatus(@PathVariable Long id, @RequestParam WorkRequestStatus status) {
        WorkRequest request = workRequestRepository.findById(id).orElseThrow();
        request.setStatus(status);
        return workRequestRepository.save(request);
    }
}

