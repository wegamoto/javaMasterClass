package com.wewe.autogate.controller;

import com.wewe.autogate.model.AccessLog;
import com.wewe.autogate.repository.AccessLogRepository;
import com.wewe.autogate.repository.AllowedPlateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class PlateController {

    @Autowired
    private AllowedPlateRepository allowedRepo;
    @Autowired private AccessLogRepository logRepo;

    @PostMapping("/checkPlate")
    public Map<String, Object> checkPlate(@RequestBody Map<String, String> body) {
        String plate = body.get("plateNumber");
        boolean allowed = allowedRepo.existsByPlateNumber(plate);

        AccessLog log = new AccessLog();
        log.setPlateNumber(plate);
        log.setStatus(AccessLog.AccessStatus.valueOf(allowed ? "ALLOWED" : "DENIED"));
        logRepo.save(log);

        return Map.of(
                "plate", plate,
                "status", allowed ? "ALLOWED" : "DENIED"
        );
    }
}

