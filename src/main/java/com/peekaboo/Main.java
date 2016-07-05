package com.peekaboo;

import com.peekaboo.mail.MailService;
import com.peekaboo.model.entity.User;
import com.peekaboo.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Main {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("config/root.xml");
        UserService userService = (UserService) context.getBean("userService");
        Main m = new Main();
        System.out.println(m.applicationEventPublisher.toString());
        User user = new User();
        user.setUsername("qwerty@gmail.com");
        user.setPassword("qwerty");
        userService.add(user);
        System.out.println("User were created");
        System.out.println("Fetching user with id 1");
        System.out.println(userService.findByUsername("Vlad"));
        MailService sender = (MailService)  context.getBean("mailService");
        sender.send("zakolenkoroman@gmail.com", "test", "Hello world");
    }
}
