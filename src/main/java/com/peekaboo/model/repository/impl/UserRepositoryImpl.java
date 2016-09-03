package com.peekaboo.model.repository.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peekaboo.model.Neo4jSessionFactory;
import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.relations.Friendship;
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

    public UserRepositoryImpl() {
    }

    public UserRepositoryImpl(Neo4jSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User save(User user) {
        Session session = sessionFactory.getSession();
        try {
            if (getAll().size()==1) {
                session.query("create constraint on (user:User) assert user.username is unique", Collections.EMPTY_MAP);
//            session.query("create constraint on (user:User) assert user.telephone is unique",Collections.EMPTY_MAP);
                session.query("create constraint on (user:User) assert user.email is unique",Collections.EMPTY_MAP);
            }
        }catch (NullPointerException e) {}
        try {
            session.save(user);
        } catch (Exception e) {
            return null;
        }

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
        target.getFriends().add(new Friendship(target, whom));
        session.save(target);
        whom.getFriends().add(new Friendship(whom, target));
        session.save(whom);
    }

    @Override
    public void deleteFriend(User from, User whom) {
        Session session = sessionFactory.getSession();
        Friendship friendship = session.loadAll(Friendship.class, new Filter("fromto", from.getId().toString() + whom.getId().toString())).iterator().next();
        from.getFriends().remove(friendship);
        save(from);
        friendship = session.loadAll(Friendship.class, new Filter("fromto", whom.getId().toString() + from.getId().toString())).iterator().next();
        whom.getFriends().remove(friendship);
        save(whom);
    }

    @Override
    public void addToBlackList(User from, User to) {
        Session session = sessionFactory.getSession();
        Friendship friendship = session.loadAll(Friendship.class, new Filter("fromto", to.getId().toString() + from.getId().toString())).iterator().next();
        friendship.setAvailibility(0);
        save(from);
    }

    @Override
    public void removeFromBlackList(User from, User to) {
        Session session = sessionFactory.getSession();
        Friendship friendship = session.loadAll(Friendship.class, new Filter("fromto", to.getId().toString() + from.getId().toString())).iterator().next();
        friendship.setAvailibility(1);
        save(from);
    }

    @Override
    public ArrayList<User> getFriends(User user) {
        ArrayList<User> friends = new ArrayList<>();
        user.getFriends().forEach(v -> {
            if (v.getAvailibility() == 1) {
                friends.add(v.getUserto());
            }
        });
        return friends;
    }

    @Override
    public ArrayList<User> getBlackListFriends(User user) {
        ArrayList<User> friends = new ArrayList<>();
        user.getFriends().forEach(v -> {
            if (v.getAvailibility() == 0) {
                friends.add(v.getUserto());
            }
        });
        return friends;
    }

    public int getUserState(User user) {
        return findByUsername(user.getUsername()).getState();
    }
}
