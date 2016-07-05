package com.peekaboo.mail;

import org.springframework.mail.SimpleMailMessage;

public interface Sender {

    void send(String to, String subject, String body);

    void send(SimpleMailMessage mailMessage);
}
