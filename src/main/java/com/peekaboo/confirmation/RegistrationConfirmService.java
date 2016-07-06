package com.peekaboo.confirmation;

import com.peekaboo.confirmation.mail.MailService;
import com.peekaboo.confirmation.phone.PhoneService;
import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegistrationConfirmService {

    @Autowired
    private MailService mailService;

    @Autowired
    private PhoneService phoneService;

    public void confirm(User user, VerificationToken token) {
        ConfirmAction confirmAction;
        if (user.getUsername().contains("@")) {
            confirmAction = new ConfirmAction(user, token, mailService);
        } else {
            confirmAction = new ConfirmAction(user, token, phoneService);
        }
        confirmAction.start();
    }
}