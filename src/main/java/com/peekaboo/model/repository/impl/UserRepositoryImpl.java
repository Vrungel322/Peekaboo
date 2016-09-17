package com.peekaboo.model.repository.impl;

import com.peekaboo.model.Neo4jSessionFactory;
import com.peekaboo.model.entity.Storage;
import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.relations.Friendship;
import com.peekaboo.model.entity.relations.PendingFriendship;
import com.peekaboo.model.entity.relations.PendingMessages;
import com.peekaboo.model.repository.UserRepository;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.cypher.Filters;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

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
            if (getAll().size() == 1) {
                session.query("create constraint on (user:User) assert user.username is unique", Collections.EMPTY_MAP);
//            session.query("create constraint on (user:User) assert user.telephone is unique",Collections.EMPTY_MAP);
                session.query("create constraint on (user:User) assert user.email is unique", Collections.EMPTY_MAP);
            }
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
        try{
            return session.loadAll(User.class, new Filter("username", username))
                .stream().findFirst().get();
        }catch (Exception ex){ return null;}
    }

    @Override
    public User findByEmail(String email) {
        Session session = sessionFactory.getSession();
        try{
            return session.loadAll(User.class, new Filter("email", email))
                    .stream().findFirst().get();
        }catch (Exception ex){ return null;}
    }

    @Override
    public User findByTelephone(String telephone) {
        Session session = sessionFactory.getSession();
        try{
            return session.loadAll(User.class, new Filter("telephone", telephone))
                    .stream().findFirst().get();
        }catch (Exception ex){ return null;}
    }

    @Override
    public List<User> getAll() {
        Session session = sessionFactory.getSession();
        return session.loadAll(User.class).stream().collect(Collectors.toList());
    }

    @Override
    public void clearDataBase() {
        Session session = sessionFactory.getSession();
        session.purgeDatabase();
    }

    @Override
    public void addNewFriend(User target, User whom) {
        Session session = sessionFactory.getSession();
        deleteFriendshipRequest(target,whom);
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
    public List<User> getFriends(User user) {
        List<User> friends = new ArrayList<>();
        user.getFriends().forEach(v -> {
            if (v.getAvailibility() == 1) {
                friends.add(v.getUserto());
            }
        });
        return friends;
    }

    @Override
    public List<User> getBlackListFriends(User user) {
        List<User> friends = new ArrayList<>();
        user.getFriends().forEach(v -> {
            if (v.getAvailibility() == 0) {
                friends.add(v.getUserto());
            }
        });
        return friends;
    }

    @Override
    public void addPendingMessage(User from, User target,String type, String object) {
        Session session = sessionFactory.getSession();
        try {
            PendingMessages pendings = from.getPendingMessages().stream()
                    .filter(x -> x.getFromto().equals(from.getId().toString() + target.getId().toString()))
                    .findFirst().get();
            LinkedList<String> messages = (LinkedList<String>) pendings.getMessages();
            messages.add(object);
            pendings.setMessages(messages);
            pendings.setType(type);
            from.getPendingMessages().add(pendings);
        } catch (Exception e) {
            from.getPendingMessages().add(new PendingMessages(from, target,type, object));
        }
        session.save(from);
        session.save(target);
    }

    @Override
    public Map<String, List<String>> getPendingMessagesFor(User target) {
        List<User> pendings = getAll().stream()
                .filter(f -> f.wantsToSendMessages(target.getUsername()) == true).collect(Collectors.toList());
        Map<String, List<String>> resultPendings = new HashMap<>();
        pendings.forEach(p -> {
            resultPendings.put(p.getUsername(),(List<String>) p.getPendingMessagesFor(target.getUsername()));
        });
        return resultPendings;
    }

    @Override
    public void deleteProfilePhoto(User user) {
        Session session = sessionFactory.getSession();
        User curUser = findByUsername(user.getUsername());
        session.delete(curUser.getProfilePhoto());
        curUser.setProfilePhoto(null);
        update(curUser);
    }

    @Override
    public void sendFriendshipRequest(User user, User target) {
        Session session = sessionFactory.getSession();
        user.getRequestFriends().add(new PendingFriendship(user, target));
        session.save(user);
    }

    @Override
    public void deleteFriendshipRequest(User user, User target) {
        Session session = sessionFactory.getSession();
        try {
            PendingFriendship pendingFriendship = session.loadAll(PendingFriendship.class, new Filter("fromto", user.getId().toString() + target.getId().toString())).iterator().next();
            user.getRequestFriends().remove(pendingFriendship);
            update(user);
        } catch (Exception e) {}

        try {
            PendingFriendship pendingFriendship = session.loadAll(PendingFriendship.class, new Filter("fromto", target.getId().toString() + user.getId().toString())).iterator().next();
            target.getRequestFriends().remove(pendingFriendship);
            update(target);
        } catch (Exception e) {}


    }

    @Override
    public void changeProfilePhoto(User user, Storage avatar) {
        User curUser = findByUsername(user.getUsername());
        if (curUser.getProfilePhoto() != null) {
            deleteProfilePhoto(curUser);
        }
        curUser.setProfilePhoto(avatar);
        update(curUser);
    }

    public int getUserState(User user) {
        return findByUsername(user.getUsername()).getState();
    }

    @Override
    public void deletePendingMessages(User target) {
        Session session = sessionFactory.getSession();
        List<User> pendings = getAll().stream()
                .filter(f -> f.wantsToSendMessages(target.getUsername()) == true).collect(Collectors.toList());
        pendings.forEach(p -> p.getPendingMessages().forEach(v-> session.delete(v)));
    }
}
