package com.peekaboo.model.service;

import com.peekaboo.model.Neo4jSessionFactory;
import com.peekaboo.model.entity.Storage;
import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.VerificationToken;
import com.peekaboo.model.repository.impl.StorageRepositoryImpl;
import com.peekaboo.model.repository.impl.UserRepositoryImpl;
import com.peekaboo.model.repository.impl.VerificationRepositoryImpl;
import com.peekaboo.model.service.impl.StorageServiceImpl;
import com.peekaboo.model.service.impl.UserServiceImpl;
import org.neo4j.graphdb.Node;
import org.neo4j.ogm.session.Session;

import java.util.ArrayList;
import java.util.Locale;

public class TestNeo4jConnection {
    public static void main(String[] args) {

        UserRepositoryImpl userRepository = new UserRepositoryImpl(new Neo4jSessionFactory());
        userRepository.clearDataBase();
    }
}
