package com.wewe.autogate.controller;

import com.wewe.autogate.model.AccessLog;
import com.wewe.autogate.model.AllowedPlate;
import com.wewe.autogate.repository.AccessLogRepository;
import com.wewe.autogate.repository.AllowedPlateRepository;
import com.wewe.autogate.service.GateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gate")
public class GateController {

    @Autowired
    private AllowedPlateRepository allowedPlateRepository;

    @Autowired
    private AccessLogRepository accessLogRepository;

    @Autowired
    private  GateService gateService;

    // ✅ ตรวจสอบป้ายทะเบียน
    @GetMapping("/check")
    public ResponseEntity<String> checkPlate(@RequestParam String plate) {
        boolean allowed = allowedPlateRepository.existsByPlateNumber(plate);

        if (allowed) {
            return ResponseEntity.ok("ACCESS_GRANTED");
        } else {
            return ResponseEntity.status(403).body("ACCESS_DENIED");
        }
    }

    // ✅ เพิ่มทะเบียนรถใหม่
    @PostMapping("/add")
    public ResponseEntity<String> addPlate(@RequestBody AllowedPlate plate) {
        if (allowedPlateRepository.existsByPlateNumber(plate.getPlateNumber())) {
            return ResponseEntity.badRequest().body("PLATE_ALREADY_EXISTS");
        }

        allowedPlateRepository.save(plate);
        return ResponseEntity.ok("PLATE_ADDED");
    }

    // ✅ ดูรายการทั้งหมด
    @GetMapping("/list")
    public List<AllowedPlate> getAllPlates() {
        return allowedPlateRepository.findAll();
    }

    @GetMapping("/logs")
    public List<AccessLog> getLogs() {
        return accessLogRepository.findAll();
    }

}

