package com.wewe.crudapi.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LineNotifyService {

    private static final String LINE_NOTIFY_API = "https://notify-api.line.me/api/notify";
    private static final String LINE_TOKEN = "mgvARiShcv7nIlFRpMTcsJd7ZZrT09iH3AGMLaQSCxO"; // เปลี่ยนเป็น Token ของคุณ

    public void sendLineNotify(String message) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + LINE_TOKEN);
            headers.set("Content-Type", "application/x-www-form-urlencoded");

            String body = "message=" + message;
            HttpEntity<String> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(LINE_NOTIFY_API, HttpMethod.POST, request, String.class);

            System.out.println("LINE Notify Response: " + response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

