package com.peekaboo.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailMessage;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class MailService implements Sender {

    @Autowired
    MailSender mailSender;

    @Override
    public void send(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        send(message);
    }

    @Override
    public void send(SimpleMailMessage mailMessage) {
        mailSender.send(mailMessage);
    }
}
