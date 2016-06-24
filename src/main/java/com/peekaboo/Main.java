package com.peekaboo;

import com.peekaboo.entity.User;
import com.peekaboo.repository.UserRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("config/root.xml");
        UserRepository userRepository = context.getBean(UserRepository.class);

        User user = new User();
        user.setName("Vlad");
        userRepository.create(user);

        System.out.println("User were created");
        System.out.println("Fetching user with id 1");
        System.out.println(userRepository.getByKey(1l));



    }
}
