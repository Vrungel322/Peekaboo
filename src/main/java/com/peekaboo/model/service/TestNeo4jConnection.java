package com.peekaboo.model.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TestNeo4jConnection {
    static Neo4jSessionFactory neo4jSessionFactory = new Neo4jSessionFactory();
    static UserRepositoryImpl userService = new UserRepositoryImpl(neo4jSessionFactory);
    static VerificationRepositoryImpl verificationRepository = new VerificationRepositoryImpl(neo4jSessionFactory);
    static StorageRepositoryImpl storageRepository = new StorageRepositoryImpl(neo4jSessionFactory);

    public static void main(String[] args) {


    }
}
