package com.peekaboo.model.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.peekaboo.confirmation.RegistrationConfirmService;
import com.peekaboo.controller.sign.SignResponse;
import com.peekaboo.model.entity.Storage;
import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.VerificationToken;
import com.peekaboo.model.entity.enums.UserRole;
import com.peekaboo.model.repository.StorageRepository;
import com.peekaboo.model.repository.UserRepository;
import com.peekaboo.model.repository.VerificationTokenRepository;
import com.peekaboo.security.jwt.JwtUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/config/rootTest.xml"})
public class UserServiceTest {

    static String userID = "";
    static String tokenID = "";
    final String username = "rtwnk";
    final String pwrd = "rtwnk1234";
    final String login = "rtwnk@gmail.com";
    private final Logger logger = LogManager.getLogger(this);

    @Autowired
    UserRepository userService;
    @Autowired
    VerificationTokenRepository verificationService;
    @Autowired
    RegistrationConfirmService registrationConfirmService;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    BCryptPasswordEncoder encoder;
    @Autowired
    StorageRepository storageService;

    @Test
    public void createUpdateAndDeleteUser() {
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
        userService.save(new User("maks", "maksim", "sss", "asdad", "maksratosh@gmail.com", 0, 0, true, 0));
        userService.save(new User("alex1", "sashka1", "sss", "asdad1", "alex1@gmail.com", 0, 0, false, 0));
        userService.save(new User("alex2", "sashka2", "sss", "asdad2", "alex2@gmail.com", 0, 0, false, 0));
        userService.save(new User("alex3", "sashka3", "sss", "asdad3", "alex3@gmail.com", 0, 0, false, 0));
        userService.save(new User("alex4", "sashka4", "sss", "asdad4", "alex4@gmail.com", 0, 0, false, 0));
        User user = userService.findByUsername("maks");
        User user1 = userService.findByUsername("alex1");
        User user2 = userService.findByUsername("alex2");
        User user3 = userService.findByUsername("alex3");
        User user4 = userService.findByUsername("alex4");
        userService.addNewFriend(user, user1);
        userService.addNewFriend(user, user2);
        userService.addNewFriend(user, user3);
        userService.addNewFriend(user, user4);
        userService.delete(user);
        userService.delete(user1);
        userService.delete(user2);
        userService.delete(user3);
        userService.delete(user4);
    }

    @Test
    public void pendingFriendshipRequest() {
        userService.save(new User("maks", "maksim", "sss", "asdad", "maksratosh@gmail.com", 0, 0, true, 0));
        userService.save(new User("alex1", "sashka1", "sss", "asdad1", "alex1@gmail.com", 0, 0, false, 0));
        userService.save(new User("alex2", "sashka2", "sss", "asdad2", "alex2@gmail.com", 0, 0, false, 0));
        userService.save(new User("alex3", "sashka3", "sss", "asdad3", "alex3@gmail.com", 0, 0, false, 0));
        userService.save(new User("alex4", "sashka4", "sss", "asdad4", "alex4@gmail.com", 0, 0, false, 0));
        User user = userService.findByUsername("maks");
        User user1 = userService.findByUsername("alex1");
        User user2 = userService.findByUsername("alex2");
        User user3 = userService.findByUsername("alex3");
        User user4 = userService.findByUsername("alex4");
        userService.sendFriendshipRequest(user1, user2);
        userService.sendFriendshipRequest(user1, user4);
        userService.sendFriendshipRequest(user4, user1);
        userService.delete(user);
        userService.delete(user1);
        userService.delete(user2);
        userService.delete(user3);
        userService.delete(user4);
    }

    @Test
    public void constraintFiledsTest() {
        User user = userService.save(new User("username", "maksim", "sss", "telephone", "email", 0, 0, true, 0));
        //check for email - failed
        User user1 = userService.save(new User("username1", "maksim", "sss", "telephone1", "emamil", 0, 0, true, 0));
        //check for username - failed
        User user2 = userService.save(new User("username", "maksim", "sss", "telephone", "email1", 0, 0, true, 0));
        //result - success
        User user3 = userService.save(new User("username1", "maksim", "sss", "telephone1", "email1", 0, 0, true, 0));
        Assert.assertEquals(2, userService.getAll().size());
        userService.delete(user);
        userService.delete(user3);

    }

    @Test
    public void avatarOperations() {
        User user = new User("username", "maksim", "sss", "telephone", "email", 0, 0, true, 0);
        userService.save(user);
        userService.changeProfilePhoto(userService.findByUsername("username"), new Storage("avatar1.jpg", "path.to.avatar"));
        userService.changeProfilePhoto(userService.findByUsername("username"), new Storage("avatar2.jpg", "path.to.avatar"));
        userService.changeProfilePhoto(userService.findByUsername("username"), new Storage("avatar3.jpg", "path.to.avatar"));
        userService.changeProfilePhoto(userService.findByUsername("username"), new Storage("avatar4.jpg", "path.to.avatar"));
        userService.changeProfilePhoto(userService.findByUsername("username"), new Storage("avatar5.jpg", "path.to.avatar"));
        userService.changeProfilePhoto(userService.findByUsername("username"), new Storage("avatar6.jpg", "path.to.avatar"));
        userService.deleteProfilePhoto(user);
        userService.changeProfilePhoto(userService.findByUsername("username"), new Storage("avatar1.jpg", "path.to.avatar"));
        userService.delete(user);
    }

    @Test
    public void pendingMessagesQueryForOfflineUser() {
        userService.save(new User("maks", "Maks Boss Backend", "sss", "asdad", "maksratosh@gmail.com", 0, 0, true, 0));
        userService.save(new User("Vasyan", "Vasyan", "sss", "asdad1", "alex1@gmail.com", 0, 0, false, 0));
        userService.save(new User("Lola", "Lola Shkoora", "sss", "asdad2", "alex2@gmail.com", 0, 0, false, 0));
        User user = userService.findByUsername("maks");
        User user1 = userService.findByUsername("Vasyan");
        User user2 = userService.findByUsername("Lola");
        userService.addNewFriend(user, user1);
        userService.addPendingMessage(user, user1, "type", new Gson().toJson(new User("maks", "Maks Boss Backend", "sss", "asdad", "maksratosh@gmail.com", 0, 0, true, 0)));
        userService.addPendingMessage(user, user1, "type", new Gson().toJson(new User("maks1", "Maks Boss Backend", "sss", "asdad", "maksratosh@gmail.com", 0, 0, true, 0)));
        userService.addPendingMessage(user, user1, "type", new Gson().toJson(new User("maks2", "Maks Boss Backend", "sss", "asdad", "maksratosh@gmail.com", 0, 0, true, 0)));
        userService.addPendingMessage(user2, user1, "type", new Gson().toJson(new User("maks3", "Maks Boss Backend", "sss", "asdad", "maksratosh@gmail.com", 0, 0, true, 0)));
        userService.addPendingMessage(user2, user1, "type", new Gson().toJson(new User("maks4", "Maks Boss Backend", "sss", "asdad", "maksratosh@gmail.com", 0, 0, true, 0)));
        userService.addPendingMessage(user2, user1, "type", new String("привет"));
        userService.addPendingMessage(user2, user1, "type", new String("я - Лола"));
        userService.addPendingMessage(user2, user1, "type", new String("хочу познакомиться"));
        userService.addPendingMessage(user, user1, "type", new String("как дела?"));
        userService.addPendingMessage(user, user1, "type", new String("что делаешь?"));
        userService.addPendingMessage(user, user1, "type", new String("смотри какие мемы мне чепурной скинул)))"));
        userService.addPendingMessage(user, user1, "type", new String("/User/Desktop/mem1.png"));
        userService.addPendingMessage(user, user1, "type", new String("/User/Desktop/mem2.png"));
        userService.addPendingMessage(user, user1, "type", new String("/User/Desktop/mem3.png"));
        userService.addPendingMessage(user, user1, "type", new String("/User/Desktop/sticker.png"));
        userService.addPendingMessage(user, user1, "type", new String("я просто угораю, это лютый треш!!!"));
        userService.getPendingMessagesFor(userService.findByUsername(user1.getUsername()))
                .forEach((k, v) -> {
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
        userService.deletePendingMessages(userService.findByUsername("Vasyan"));
        userService.delete(user);
        userService.delete(user1);
        userService.delete(user2);
    }


    @Test
    public void signupTest() {
        logger.debug("Got SIGN UP request");
        logger.debug("Attempting to register new user");
        User user = userService.findByUsername(username);
        String password = encoder.encode(pwrd);
        if (user != null) {
            logger.debug("User has registered before");
            logger.debug("Checking maybe he hasn't been verified yet");
            if (user.isEnabled()) {
                logger.error("User has already been registered. Send him error");
                logger.error("username is taken");
            }
            if (user.hasLogin(login) ||
                    !userService.loginExists(login)) {
                logger.debug("User has entered unique login or login belongs to him. Updating user info");
                user.emptyLogin();
                user.setLogin(login);
                user.setPassword(password);
                userService.update(user);
                logger.debug("Removing old verification key");
                verificationService.deleteByValue(verificationService.findByUser(user).getValue());
            } else {
                logger.error("Login is taken");
            }
        } else {
            if (userService.loginExists(login)) {
                logger.error("Login is taken");
            } else {
                user = new User();
                user.setUsername(username);
                user.setname(user.getUsername());
                user.setLogin(login);
                user.setPassword(password);
                user.setEnabled(true);
                user.addRole(UserRole.USER);
                user = userService.save(user);
            }
        }
        VerificationToken verToken = registrationConfirmService.generateVerificationToken();
        verToken.setUser(user);
        verToken = verificationService.create(verToken);
        registrationConfirmService.confirm(user, verToken);
        logger.debug("User were successfully created");
        userID = userService.findByUsername(username).getId().toString();
        tokenID = verToken.getValue().toString();
        signConfirmation();
    }

    @Test
    public void signConfirmation() {
        logger.debug("Got CONFIRMATION request");
        VerificationToken key = verificationService.findByValue(tokenID);
        if (key == null || !key.getUser().getId().toString().equals(userID)) {
            logger.debug("User entered invalid verification token");
        } else {
            User user = userService.findById(Long.valueOf(userID));
            user.setEnabled(true);
            verificationService.deleteByValue(tokenID);
            userService.update(user);
            SignResponse response = new SignResponse();
            response.setId(user.getId().toString())
                    .setUsername(user.getUsername())
                    .setRole(user.getRoles())
                    .setEnabled(user.isEnabled());
            userService.delete(user);
        }
    }


    public void signinTest() {
        logger.debug("Got SIGN IN request");
        User user;
        user = userService.findByUsername(username);
        String password = pwrd;
        if (user == null || !encoder.matches(password, user.getPassword())) {
            logger.debug("User has entered wrong login or password. Sending NOT_FOUND response");
            logger.error("User entered wrong login or password");
        }
        logger.debug("User were successfully authorized");
        SignResponse response = new SignResponse();
        response.setId(user.getId().toString())
                .setUsername(user.getUsername())
                .setRole(user.getRoles())
                .setEnabled(true);
        String token = jwtUtil.generateToken(response);
        logger.error(token);
    }

    @Test
    public void photoTest() {
        userService.clearDataBase();
        //TODO: You must have existed photo file
        String filePath = System.getProperty("user.dir") + File.separator + "tmp" + File.separator + "default_profile_photo.jpg";

        if (storageService.findByFileName("default_profile_photo") == null) {
            //TODO: Add correct file path
            Storage storage = new Storage("default_profile_photo", filePath);
            storageService.save(storage);
        }
        User user = new User("dsfgcbnh", "sdxvcbn", "sss", "asdad2", "dbvn@gmail.com", 0, 0, false, 0);
        userService.save(user);


        System.out.println(storageService.findByFileName("default_profile_photo").toString());
        System.out.println(userService.findByEmail(user.getEmail()).getProfilePhoto().getFileName());
        Assert.assertEquals(userService.findByEmail(user.getEmail()).getProfilePhoto().getFilePath(),
                storageService.findByFileName("default_profile_photo").getFilePath());
    }
}
