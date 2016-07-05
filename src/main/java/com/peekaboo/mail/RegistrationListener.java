package com.peekaboo.mail;

import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.VerificationToken;
import com.peekaboo.model.service.UserService;
import com.peekaboo.model.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<RegistrationConfirmEvent> {
    public static final String REGISTRATION_CONFIRMATION = "Registration Confirmation";

    @Value("application.host")
    private String host;

    @Value("application.url")
    private String url;

    @Autowired
    private MailService mailService;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Override
    public void onApplicationEvent(RegistrationConfirmEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(RegistrationConfirmEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        verificationTokenService.add(new VerificationToken(token, user));
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getUsername());
        mail.setSubject(REGISTRATION_CONFIRMATION);
        mail.setText("http://" + host + url + "registration/confirm/" + token);
        mailService.send(mail);
    }
}
