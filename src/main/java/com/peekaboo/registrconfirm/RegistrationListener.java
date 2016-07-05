package com.peekaboo.registrconfirm;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class RegistrationListener implements ApplicationListener<ConfirmEvent> {

    @Override
    public void onApplicationEvent(ConfirmEvent event) {
        event.getConfirmSender().send(event.getUser().getUsername(), event.getVerificationToken().getValue());
    }
}
