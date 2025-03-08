package com.wewe.crudapi.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendConfirmationEmail(String toEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("ขอขอบคุณที่สนใจติดตั้งแผงโซล่าเซลล์");
        message.setText("เรียนลูกค้า, \n\nขอขอบคุณที่สนใจติดตั้งแผงโซลาร์เซลล์ ทีมงานของเราจะติดต่อกลับโดยเร็วที่สุด");

        mailSender.send(message);
    }
}

