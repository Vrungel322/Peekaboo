package com.peekaboo.model.service;

import com.peekaboo.model.entity.User;

import java.util.ArrayList;

public interface UserService extends EntityService<User, String> {

    User findByLogin(String login);
    User save(User user);
    void update(User user);
    void delete(User user);
    User findById(Long id);
    User findByUsername(String username);
    boolean loginExists(String login);
    User findByEmail(String email);
    User findByTelephone(String telephone);
    ArrayList<User> getAll();
    void clearDataBase();
    ArrayList<User> getFriends(User user);
    void addNewFriend(User target, User whom);
    void deleteFriend(User from, User to);
    void addToBlackList(User from, User to);
    void removeFromBlackList(User from, User to);
    ArrayList<User> getBlackListFriends(User user);



}
