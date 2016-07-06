package com.peekaboo.confirmation;

import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.VerificationToken;
import org.springframework.context.ApplicationEvent;

public class ConfirmEvent extends ApplicationEvent {
    private final User user;
    private final VerificationToken verificationToken;
    private final ConfirmSender confirmSender;

    public ConfirmEvent(Object source, User user, VerificationToken verificationToken,
                        ConfirmSender confirmSender) {
        super(source);
        this.user = user;
        this.verificationToken = verificationToken;
        this.confirmSender = confirmSender;
    }

    public User getUser() {
        return user;
    }

    public VerificationToken getVerificationToken() {
        return verificationToken;
    }

    public ConfirmSender getConfirmSender() {
        return confirmSender;
    }
}
