package com.peekaboo.model;

import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.stereotype.Component;

@Component(value = "sessionFactory")
public class Neo4jSessionFactory {

    public static final String NEO4J_URL = "http://95.85.24.64:7474";
    public static final String USERNAME = "neo4j";
    public static final String PASSWORD = "developer";
    public static final String PACKAGE = "com.peekaboo.model";
    private SessionFactory sessionFactory = new SessionFactory(PACKAGE);
    private Session session = sessionFactory.openSession(NEO4J_URL, USERNAME, PASSWORD);

    public Neo4jSessionFactory() {
    }

    public Session getSession() {
        return session;
    }

}
