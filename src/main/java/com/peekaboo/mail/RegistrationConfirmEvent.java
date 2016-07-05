package com.peekaboo.mail;

import com.peekaboo.model.entity.User;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class RegistrationConfirmEvent extends ApplicationEvent {
    private final String appUrl;
    private final Locale locale;
    private final User user;

    public RegistrationConfirmEvent(Object source, String appUrl, Locale locale, User user) {
        super(source);
        this.appUrl = appUrl;
        this.locale = locale;
        this.user = user;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public Locale getLocale() {
        return locale;
    }

    public User getUser() {
        return user;
    }
}
