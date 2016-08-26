package com.peekaboo.model.repository.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peekaboo.model.Neo4jSessionFactory;
import com.peekaboo.model.entity.User;
import com.peekaboo.model.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.cypher.Filters;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private Neo4jSessionFactory sessionFactory;

    public UserRepositoryImpl() {}
    public UserRepositoryImpl(Neo4jSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User save(User user) {
        Session session = sessionFactory.getSession();
        session.save(user);
        return user;
    }

    @Override
    public User update(User user) {
        Session session = sessionFactory.getSession();
        session.save(user);
        return user;
    }

    @Override
    public void delete(User user) {
        Session session = sessionFactory.getSession();
        session.delete(user);
    }

    @Override
    public User findById(Long id) {
        Session session = sessionFactory.getSession();
        return session.load(User.class, id);
    }

    @Override
    public User findByUsername(String username) {
        Session session = sessionFactory.getSession();
        Collection<User> res = session.loadAll(User.class, new Filter("username", username));
        if (res.isEmpty()) {
            return null;
        }
        return res.iterator().next();
    }

    @Override
    public User findByEmail(String email) {
        Session session = sessionFactory.getSession();
        Collection<User> res = session.loadAll(User.class, new Filters().add("email", email));
        if (!res.isEmpty()) {
            return res.iterator().next();
        }
        return null;
    }

    @Override
    public User findByTelephone(String telephone) {
        Session session = sessionFactory.getSession();
        Collection<User> res = session.loadAll(User.class, new Filters().add("telephone", telephone));
        if (!res.isEmpty()) {
            return res.iterator().next();
        }
        return null;
    }

    @Override
    public ArrayList<User> getAll() {
        Session session = sessionFactory.getSession();
        Collection<User> res = session.loadAll(User.class);
        ArrayList<User> resList = new ArrayList<>(res);
        if (resList.size() == 0) {
            return null;
        }
        return resList;
    }

    @Override
    public void clearDataBase() {
        Session session = sessionFactory.getSession();
        session.purgeDatabase();
    }

    @Override
    public void addNewFriend(User target, User whom) {
        Session session = sessionFactory.getSession();
        ArrayList<User> targetfriends = (ArrayList<User>) target.getFriends();
        targetfriends.add(whom);
        target.setFriends(targetfriends);
        session.save(target);
        ArrayList<User> whomfriends = (ArrayList<User>) whom.getFriends();
        whomfriends.add(target);
        whom.setFriends(whomfriends);
        session.save(whom);
    }

    @Override
    public void deleteFriend(User from, User whom) {
        Session session = sessionFactory.getSession();
        from.getFriends().remove(whom);
        session.save(from);
    }

    @Override
    public void addToBlackList(User from, User to) {
        Session session = sessionFactory.getSession();
        String targetid = from.getId().toString();
        String whomid = to.getId().toString();
        String query = "MATCH (m:User)-[r:FRIENDS]-(n:User) " +
                "WHERE ID(m)=" + targetid + " AND ID(n)=" + whomid + "\n" +
                "AND r.from=" + targetid + "\n" +
                "AND r.to=" + whomid + "\n" +
                "        SET r.availability=0";
        session.query(query, Collections.EMPTY_MAP);
    }

    @Override
    public void removeFromBlackList(User from, User to) {
        Session session = sessionFactory.getSession();
        String targetid = from.getId().toString();
        String whomid = to.getId().toString();
        String query = "MATCH (m:User)-[r:FRIENDS]-(n:User) " +
                "WHERE ID(m)=" + targetid + " AND ID(n)=" + whomid + "\n" +
                "AND r.from=" + targetid + "\n" +
                "AND r.to=" + whomid + "\n" +
                "        SET r.availability=1";
        session.query(query, Collections.EMPTY_MAP);
    }

    @Override
    public ArrayList<User> getFriends(User user) {
        return (ArrayList<User>) user.getFriends();
    }

    @Override
    public ArrayList<User> getBlackListFriends(User user) {
        String query = "MATCH (u:User)-[r:FRIENDS]-(m:User) WHERE ID(u) ="
                + user.getId().toString() + " AND r.availability='0' RETURN m;";
        Map<String, String> map = new HashMap<>();
        Iterator<User> users = sessionFactory.getSession().query(User.class, query, map).iterator();
        ArrayList<User> friends = new ArrayList<>();
        while (users.hasNext()) {
            friends.add(users.next());
        }
        return friends;
    }

    public int getUserState(User user) {
        return findByUsername(user.getUsername()).getState();
    }
}
