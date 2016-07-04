package com.peekaboo;

import com.peekaboo.mail.MailService;
import com.peekaboo.model.entity.User;
import com.peekaboo.model.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("config/root.xml");
        UserService userService = (UserService) context.getBean("userService");
        User user = new User();
        user.setUsername("Vlad");
        user.setEmail("jack@gmail.com");
        user.setPassword("qwerty");
        userService.add(user);
        System.out.println("User were created");
        System.out.println("Fetching user with id 1");
        System.out.println(userService.findByUsername("Vlad"));
        MailService sender = (MailService)  context.getBean("mailService");
        sender.send("zakolenkoroman@gmail.com", "test", "Hello world");
    }
}
