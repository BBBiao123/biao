package com.biao.web.controller;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class MailController {

    MailSender mailSender;

    @RequestMapping(path = "/email/trigger", method = RequestMethod.POST)
    public String triggerEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText("Hello from Spring Boot Application");
        message.setTo("prashantkhanal@gmail.com");
        message.setFrom("prashantkhanal@gmail.com");
        try {
            mailSender.send(message);
            return "{\"message\": \"OK\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"message\": \"Error\"}";
        }
    }
}
