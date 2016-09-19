package com.peekaboo.confirmation;

import com.peekaboo.confirmation.mail.MailService;
import com.peekaboo.confirmation.phone.PhoneService;
import com.peekaboo.messaging.socket.middleware.BinaryMessageInterceptor;
import com.peekaboo.messaging.socket.middleware.MessageInterceptor;
import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RegistrationConfirmService {
    public final static int VERIFICATION_TOKEN_LENGTH = 4;

    @Autowired
    private MailService mailService;

    @Autowired
    private PhoneService phoneService;

    public void confirm(User user, VerificationToken token) {
        ConfirmAction confirmAction;
        if (user.getLogin().contains("@")) {
            confirmAction = new ConfirmAction(user, token, mailService);
        } else {
            confirmAction = new ConfirmAction(user, token, phoneService);
        }
        confirmAction.start();

    }

    public VerificationToken generateVerificationToken() {
        VerificationToken verificationToken = new VerificationToken();
        Random random = new Random();
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < VERIFICATION_TOKEN_LENGTH; i++) {
            token.append(random.nextInt(10));
        }
        verificationToken.setValue(token.toString());
        System.out.println(token.toString());
        return verificationToken;
    }
}