package com.peekaboo.registrconfirm;

import com.peekaboo.model.entity.User;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class RegistrationConfirmEvent extends ApplicationEvent {
    private final User user;
    private final ConfirmSender confirmSender;

    public RegistrationConfirmEvent(Object source, User user,
                                    ConfirmSender confirmSender) {
        super(source);
        this.user = user;
        this.confirmSender = confirmSender;
    }

    public User getUser() {
        return user;
    }

    public ConfirmSender getConfirmSender() {
        return confirmSender;
    }
}
