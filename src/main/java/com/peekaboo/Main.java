package com.peekaboo;

import com.peekaboo.model.entity.User;
import com.peekaboo.model.service.UserService;
import com.peekaboo.registrconfirm.RegistrationConfirmEvent;
import com.peekaboo.registrconfirm.RegistrationConfirmPublisher;
import com.peekaboo.registrconfirm.mail.MailService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("config/root.xml");
        UserService userService = (UserService) context.getBean("userService");
        RegistrationConfirmPublisher publisher = (RegistrationConfirmPublisher) context.getBean("registrationConfirmPublisher");
        MailService mailService = context.getBean(MailService.class);
        User user = new User();
        user.setUsername("zakolenkoroman@gmail.com");
        user.setPassword("qwerty");
        userService.add(user);
        publisher.publishEvent(new RegistrationConfirmEvent(new Object(), user, mailService));
    }
}
