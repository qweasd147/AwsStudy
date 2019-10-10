package com.example.demo.mail;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import org.springframework.stereotype.Component;

@Component
public class MailSender {

    public MailSender(AmazonSimpleEmailService amazonSimpleEmailService) {
    }
}
