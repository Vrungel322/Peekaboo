package com.peekaboo.confirmation.mail;

import com.peekaboo.confirmation.ConfirmSender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class MailService implements ConfirmSender {
    public static final String REGISTRATION_CONFIRMATION = "Registration Confirmation";

    @Autowired
    private MailSender mailSender;

    private final Logger logger = LogManager.getLogger(MailService.class);

    @Override
    public void send(String to, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(REGISTRATION_CONFIRMATION);
        message.setText(body);
        send(message);
    }

    public void send(SimpleMailMessage mailMessage) {
        logger.debug("Start send message");
        try {
            mailSender.send(mailMessage);
        } catch (MailSendException e) {
            logger.warn("Cannot send message, retry after 100 ms");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            send(mailMessage);
        }
    }
}