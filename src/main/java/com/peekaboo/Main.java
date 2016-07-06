package com.peekaboo;

import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.VerificationToken;
import com.peekaboo.model.service.UserService;
import com.peekaboo.confirmation.ConfirmEvent;
import com.peekaboo.confirmation.RegistrationConfirmPublisher;
import com.peekaboo.confirmation.mail.MailService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("config/root.xml");
        UserService userService = (UserService) context.getBean("userService");
        RegistrationConfirmPublisher publisher = (RegistrationConfirmPublisher) context.getBean("registrationConfirmPublisher");
        MailService mailService = context.getBean(MailService.class);
        User user = new User();
        user.setLogin("zakolenkoroman@gmail.com");
        user.setPassword("qwerty");
        userService.create(user);
        VerificationToken token = new VerificationToken("test", user);
        publisher.publishEvent(new ConfirmEvent(new Object(), user, token, mailService));
    }
}
