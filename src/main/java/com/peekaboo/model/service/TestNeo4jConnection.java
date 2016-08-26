package com.peekaboo.model.service;

import com.peekaboo.model.Neo4jSessionFactory;
import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.VerificationToken;
import com.peekaboo.model.repository.impl.UserRepositoryImpl;
import com.peekaboo.model.repository.impl.VerificationRepositoryImpl;
import org.neo4j.graphdb.Node;

import java.util.ArrayList;
import java.util.Locale;

public class TestNeo4jConnection {
    public static void main(String[] args) {
        UserRepositoryImpl userRepository = new UserRepositoryImpl(new Neo4jSessionFactory());
        VerificationRepositoryImpl verificationRepository = new VerificationRepositoryImpl(new Neo4jSessionFactory());

//        userRepository.clearDataBase();
//
        User user1 = new User("user1","name1","password","1234","email1@gmail.com",0,1,true,3);
        User user2 = new User("user2","name2","password","1234","email2@gmail.com",0,1,true,3);
        User user3 = new User("user3","name3","password","1234","email3@gmail.com",0,1,true,3);
        User user4 = new User("user4","name4","password","1234","email4@gmail.com",0,1,true,3);
//
//        userRepository.save(user1);
//        userRepository.save(user2);
//        userRepository.save(user3);
//        userRepository.save(user4);
//
//        userRepository.addNewFriend(user1,user3);

        User newuser1 = userRepository.findByUsername(user1.getUsername());
//        System.out.println(newuser1.getState());
        newuser1.setEnabled(false);
        userRepository.save(newuser1);
        newuser1 = userRepository.findByUsername(user1.getUsername());
        System.out.println(newuser1.toString());

    }


}
