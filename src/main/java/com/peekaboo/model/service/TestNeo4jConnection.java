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

        userRepository.clearDataBase();

    }


}
