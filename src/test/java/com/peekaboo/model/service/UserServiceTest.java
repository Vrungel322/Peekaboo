package com.peekaboo.model.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.peekaboo.model.Neo4jSessionFactory;
import com.peekaboo.model.entity.Storage;
import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.relations.PendingMessages;
import com.peekaboo.model.repository.UserRepository;
import com.peekaboo.model.repository.impl.StorageRepositoryImpl;
import com.peekaboo.model.repository.impl.UserRepositoryImpl;
import com.peekaboo.model.repository.impl.VerificationRepositoryImpl;
import com.peekaboo.model.service.impl.UserServiceImpl;
import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/config/rootTest.xml"})
public class UserServiceTest {

    Neo4jSessionFactory neo4jSessionFactory = new Neo4jSessionFactory();
    @Autowired
    UserRepository userService;
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
        userService.save(new User("alex1", "sashka1", "sss", "asdad1", "alex1@gmail.com", 0, 0, false, 0));
        userService.save(new User("alex2", "sashka2", "sss", "asdad", "alex2@gmail.com", 0, 0, false, 0));
        userService.save(new User("alex3", "sashka3", "sss", "asdad", "alex3@gmail.com", 0, 0, false, 0));
        userService.save(new User("alex4", "sashka4", "sss", "asdad", "alex4@gmail.com", 0, 0, false, 0));
        User user = userService.findByUsername("maks");
        User user1 = userService.findByUsername("alex1");
        User user2 = userService.findByUsername("alex2");
        User user3 = userService.findByUsername("alex3");
        User user4 = userService.findByUsername("alex4");
        System.out.println(user.toString());
        System.out.println(user1.toString());
        userService.addNewFriend(user, user1);
        userService.addNewFriend(user, user2);
        userService.addNewFriend(user, user3);
        userService.addNewFriend(user, user4);
    }

    @Test
    public void constraintFiledsTest() {
        userService.clearDataBase();
        userService.save(new User("username", "maksim", "sss", "telephone", "email", 0, 0, true, 0));
        //check for email - failed
        userService.save(new User("username1", "maksim", "sss", "telephone1", "emamil", 0, 0, true, 0));
        //check for username - failed
        userService.save(new User("username", "maksim", "sss", "telephone", "email1", 0, 0, true, 0));
        //result - success
        userService.save(new User("username1", "maksim", "sss", "telephone1", "email1", 0, 0, true, 0));
        Assert.assertEquals(2,userService.getAll().size());
    }

    @Test
    public void avatarOperations(){
        userService.clearDataBase();
        User user = new User("username", "maksim", "sss", "telephone", "email", 0, 0, true, 0);
        userService.save(user);
        Storage avatar = new Storage("sdfgh", "dfgfg");
        Storage storage = new Storage("dsfvb", "sdfgf");
        user.setAvatar(avatar);
        user.getOwnStorages().add(storage);
        userService.update(user);
    }

    @Test
    public void pendingMessagesQueryForOfflineUser() {
        userService.clearDataBase();
        userService.save(new User("maks", "Maks Boss Backend", "sss", "asdad", "maksratosh@gmail.com", 0, 0, true, 0));
        userService.save(new User("Vasyan", "Vasyan", "sss", "asdad1", "alex1@gmail.com", 0, 0, false, 0));
        userService.save(new User("Lola", "Lola Shkoora", "sss", "asdad2", "alex2@gmail.com", 0, 0, false, 0));
        User user = userService.findByUsername("maks");
        User user1 = userService.findByUsername("Vasyan");
        User user2 = userService.findByUsername("Lola");
        userService.addNewFriend(user,user1);
        userService.addNewFriend(user2  ,user1);
        userService.addPendingMessage(user, user1, "type",new Gson().toJson(new User("maks", "Maks Boss Backend", "sss", "asdad", "maksratosh@gmail.com", 0, 0, true, 0)));
        userService.addPendingMessage(user, user1, "type",new Gson().toJson(new User("maks1", "Maks Boss Backend", "sss", "asdad", "maksratosh@gmail.com", 0, 0, true, 0)));
        userService.addPendingMessage(user, user1, "type",new Gson().toJson(new User("maks2", "Maks Boss Backend", "sss", "asdad", "maksratosh@gmail.com", 0, 0, true, 0)));
        userService.addPendingMessage(user2, user1, "type",new Gson().toJson(new User("maks3", "Maks Boss Backend", "sss", "asdad", "maksratosh@gmail.com", 0, 0, true, 0)));
        userService.addPendingMessage(user2, user1, "type",new Gson().toJson(new User("maks4", "Maks Boss Backend", "sss", "asdad", "maksratosh@gmail.com", 0, 0, true, 0)));
        userService.addPendingMessage(user2, user1,"type", new String("привет"));
        userService.addPendingMessage(user2, user1,"type", new String("я - Лола"));
        userService.addPendingMessage(user2, user1,"type", new String("хочу познакомиться"));
        userService.addPendingMessage(user, user1,"type", new String("как дела?"));
        userService.addPendingMessage(user, user1,"type", new String("что делаешь?"));
        userService.addPendingMessage(user, user1,"type", new String("смотри какие мемы мне чепурной скинул)))"));
        userService.addPendingMessage(user, user1,"type", new String("/User/Desktop/mem1.png"));
        userService.addPendingMessage(user, user1,"type", new String("/User/Desktop/mem2.png"));
        userService.addPendingMessage(user, user1,"type", new String("/User/Desktop/mem3.png"));
        userService.addPendingMessage(user, user1,"type", new String("/User/Desktop/sticker.png"));
        userService.addPendingMessage(user, user1,"type", new String("я просто угораю, это лютый треш!!!"));
        userService.getPendingMessagesFor(userService.findByUsername(user1.getUsername()))
                .forEach((k,v) -> {
                    System.out.println();
                    System.out.println();
                    System.out.println("unread messages from " + k);
                    v.forEach(m -> {
                        try {
                            System.out.println(new Gson().fromJson(m, JsonObject.class).get("email").toString());
                        } catch (Exception e) {
                            System.out.println(m.toString());
                        }
                    });
                    System.out.println();
                    System.out.println();
                    System.out.println();
                });
    }
}
