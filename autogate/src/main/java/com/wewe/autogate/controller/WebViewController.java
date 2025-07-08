package com.wewe.autogate.controller;

import com.wewe.autogate.model.AccessLog;
import com.wewe.autogate.repository.AccessLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class WebViewController {

    @Autowired
    private AccessLogRepository accessLogRepository;

    @GetMapping("/logs")
    public String showLogs(Model model) {
        List<AccessLog> logs = accessLogRepository.findAll();
        model.addAttribute("logs", logs);
        return "logs"; // ชื่อไฟล์ logs.html ใน /templates
    }
}

