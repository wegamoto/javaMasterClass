package com.wewe.targetcustomer.services;

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
        message.setSubject("Thank you for your interest in Solar Panel Installation");
        message.setText("Dear Customer, \n\nThank you for showing interest in installing solar panels. Our team will get back to you soon.");

        mailSender.send(message);
    }
}

