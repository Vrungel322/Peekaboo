package com.peekaboo.model.repository.impl;

import com.peekaboo.model.Neo4jSessionFactory;
import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.VerificationToken;
import com.peekaboo.model.repository.VerificationTokenRepository;
import org.neo4j.ogm.cypher.Filters;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by rtwnk on 8/22/16.
 */
@Repository
public class VerificationRepositoryImpl implements VerificationTokenRepository {

    @Autowired
    private Neo4jSessionFactory sessionFactory;

    public VerificationRepositoryImpl() {
    }

    public VerificationRepositoryImpl(Neo4jSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(VerificationToken entity) {
        Session session = sessionFactory.getSession();
        session.save(entity);
    }

    @Override
    public void delete(VerificationToken entity) {
        Session session = sessionFactory.getSession();
        session.delete(entity);
    }

    @Override
    public void update(VerificationToken verificationToken) {
        Session session = sessionFactory.getSession();
        session.save(verificationToken);
    }

    @Override
    public VerificationToken findById(Long id) {
        Session session = sessionFactory.getSession();
        return session.load(VerificationToken.class, id);
    }

    @Override
    public VerificationToken findByValue(String value) {
        Session session = sessionFactory.getSession();
        Collection<VerificationToken> res = session.loadAll(VerificationToken.class,new Filters().add("value",value));
        ArrayList<VerificationToken> resList = new ArrayList<>(res);
        if (resList.size()==0) {
            return null;
        }
        return resList.get(0);
    }

    @Override
    public void deleteByValue(String value) {
        Session session = sessionFactory.getSession();
        VerificationToken token = findByValue(value);
        session.delete(token);
    }

    @Override
    public VerificationToken findByUser(User user) {
        Session session = sessionFactory.getSession();
        Collection<VerificationToken> res = session.loadAll(VerificationToken.class);
        ArrayList<VerificationToken> resList = new ArrayList<>(res);
        for (VerificationToken token: resList) {
            try {
                User tokenUser = token.getUser();
                if (tokenUser.equals(user)) {
                    return token;
                }
            } catch (NullPointerException e) {
                continue;
            }
        }
        return null;
    }
}
