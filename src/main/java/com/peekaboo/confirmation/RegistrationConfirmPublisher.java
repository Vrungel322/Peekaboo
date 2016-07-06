package com.peekaboo.confirmation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Component
public class RegistrationConfirmPublisher implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher eventPublisher;

    @Override
    @Autowired
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

    public void publishEvent(ConfirmEvent event) {
        eventPublisher.publishEvent(event);
    }
}
