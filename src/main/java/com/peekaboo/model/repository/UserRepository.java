package com.peekaboo.model.repository;

import com.peekaboo.model.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public interface UserRepository {

    User save(User user);
    User update(User user);
    void delete(User user);
    User findById(Long id);
    User findByUsername(String username);
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
    void addPendingMessage(User from, User target,String type, Object object);
    HashMap<String, LinkedList<Object>> getPendingMessagesFor(User target);
}
