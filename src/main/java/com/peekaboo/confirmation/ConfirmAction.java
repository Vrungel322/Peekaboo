package com.peekaboo.confirmation;

import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.VerificationToken;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class ConfirmAction extends Thread {
    private final User user;
    private final VerificationToken verificationToken;
    private final ConfirmSender confirmSender;

    public ConfirmAction(User user, VerificationToken token, ConfirmSender sender) {
        this.user = user;
        this.verificationToken = token;
        this.confirmSender = sender;
    }

    @Override
    public void run() {
        confirmSender.send(user.getUsername(), verificationToken.getValue());
    }

    public User getUser() {
        return user;
    }

    public VerificationToken getVerificationToken() {
        return verificationToken;
    }
}