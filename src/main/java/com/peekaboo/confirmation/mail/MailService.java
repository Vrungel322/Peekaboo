package com.peekaboo.confirmation.mail;

import com.peekaboo.confirmation.ConfirmSender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.*;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailService implements ConfirmSender {
    public static final String REGISTRATION_CONFIRMATION = "Registration Confirmation";

    @Autowired
    private JavaMailSenderImpl mailSender;

    private final Logger logger = LogManager.getLogger(MailService.class);

    @Override
    public void send(String to, String body) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setFrom("peekaboochat@gmail.com");
            helper.setSubject(REGISTRATION_CONFIRMATION);
            helper.setText(body, true);

            send(message);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }

    public void send(MimeMessage mailMessage) {
        logger.debug("Start send message");
        boolean toSend = false;
        final int numberOfSend = 5;
        int i = 0;
        while (i < numberOfSend && !toSend) {
            try {
                logger.error(mailSender.getUsername());
                mailSender.send(mailMessage);
                toSend = true;
            } catch (MailSendException e) {
                logger.warn("Cannot send message, retry after 100 ms");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
            i++;
        }
    }
}