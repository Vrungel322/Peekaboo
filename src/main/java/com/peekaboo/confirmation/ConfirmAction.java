package com.peekaboo.confirmation;

import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.VerificationToken;

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
        System.out.println(user.getLogin());
        System.out.println(verificationToken.toString() + " null");
        confirmSender.send(user.getLogin(), verificationToken.getValue());
    }

    public User getUser() {
        return user;
    }

    public VerificationToken getVerificationToken() {
        return verificationToken;
    }
}