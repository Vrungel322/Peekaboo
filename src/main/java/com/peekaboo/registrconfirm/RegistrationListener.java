package com.peekaboo.registrconfirm;

import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.VerificationToken;
import com.peekaboo.model.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<RegistrationConfirmEvent> {

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
        event.getConfirmSender().send(user.getUsername(), token);
    }
}
