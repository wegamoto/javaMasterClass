package com.wewe.autogate.service;

import com.wewe.autogate.model.AccessLog;
import com.wewe.autogate.repository.AccessLogRepository;
import com.wewe.autogate.repository.AllowedPlateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GateService {

    @Autowired
    private AllowedPlateRepository allowedPlateRepository;

    @Autowired
    private AccessLogRepository accessLogRepository;

    public boolean checkPlateAndLog(String plateNumber) {
        boolean allowed = allowedPlateRepository.existsByPlateNumber(plateNumber);

        AccessLog log = new AccessLog();
        log.setPlateNumber(plateNumber);
        log.setStatus(allowed ? AccessLog.AccessStatus.ALLOWED : AccessLog.AccessStatus.DENIED);
        accessLogRepository.save(log);

        return allowed;
    }
}

