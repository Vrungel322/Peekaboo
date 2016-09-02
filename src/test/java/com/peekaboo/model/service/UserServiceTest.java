package com.peekaboo.model.service;

import com.peekaboo.model.Neo4jSessionFactory;
import com.peekaboo.model.entity.User;
import com.peekaboo.model.repository.impl.StorageRepositoryImpl;
import com.peekaboo.model.repository.impl.UserRepositoryImpl;
import com.peekaboo.model.repository.impl.VerificationRepositoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/config/rootTest.xml"})
public class UserServiceTest {

    Neo4jSessionFactory neo4jSessionFactory = new Neo4jSessionFactory();
    UserRepositoryImpl userService = new UserRepositoryImpl(neo4jSessionFactory);
    VerificationRepositoryImpl verificationRepository = new VerificationRepositoryImpl(neo4jSessionFactory);
    StorageRepositoryImpl storageRepository = new StorageRepositoryImpl(neo4jSessionFactory);

    @Test
    public void createUpdateAndDeleteUser() {
        userService.clearDataBase();
        User user = new User("maks", "maksim", "sss", "asdad", "maksratosh@g" +
                "mail.com", 0, 0, true, 0);
        userService.save(user);
        user = userService.findByUsername(user.getUsername());
        user.setUsername("jack");
        userService.update(user);
        User u = userService.findByUsername("jack");
        System.out.println(u.toString());
        Assert.assertEquals(u.getUsername(), "jack");
        userService.delete(userService.findByUsername("jack"));
    }

    @Test
    public void managingWithFriends() {
        userService.clearDataBase();
        userService.save(new User("maks", "maksim", "sss", "asdad", "maksratosh@gmail.com", 0, 0, true, 0));
        userService.save(new User("alex1", "sashka1", "sss", "asdad", "alex1@gmail.com", 0, 0, false, 0));
        userService.save(new User("alex2", "sashka2", "sss", "asdad", "alex2@gmail.com", 0, 0, false, 0));
        userService.save(new User("alex3", "sashka3", "sss", "asdad", "alex3@gmail.com", 0, 0, false, 0));
        userService.save(new User("alex4", "sashka4", "sss", "asdad", "alex4@gmail.com", 0, 0, false, 0));
        User user = userService.findByUsername("maks");
        User user1 = userService.findByUsername("alex1");
        User user2 = userService.findByUsername("alex2");
        User user3 = userService.findByUsername("alex3");
        User user4 = userService.findByUsername("alex4");
        userService.addNewFriend(user, user1);
        userService.addNewFriend(user, user2);
        userService.addNewFriend(user, user3);
        userService.addNewFriend(user, user4);
        System.out.println(userService.getFriends(user).size());
//        userService.addToBlackList(user,user1);
//        userService.deleteFriend(user, user1);
//        userService.delete(user);
//        userService.delete(user1);
    }
}
