package com.peekaboo.model.service;

import com.peekaboo.model.Neo4jSessionFactory;
import com.peekaboo.model.entity.User;
import org.neo4j.ogm.session.Session;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestNeo4jConnection {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("config/root.xml");
        Neo4jSessionFactory sessionFactory = context.getBean(Neo4jSessionFactory.class);
        Session session = sessionFactory.getSession();
        session.save(new User());
    }
}
