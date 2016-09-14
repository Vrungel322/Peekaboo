package com.peekaboo.model.repository;

import com.peekaboo.model.entity.Storage;
import com.peekaboo.model.entity.User;

import java.util.List;
import java.util.Map;

public interface UserRepository {

    User save(User user);
    User update(User user);
    void delete(User user);
    User findById(Long id);
    User findByUsername(String username);
    User findByEmail(String email);
    User findByTelephone(String telephone);
    List<User> getAll();
    void clearDataBase();
    List<User> getFriends(User user);
    void addNewFriend(User target, User whom);
    void deleteFriend(User from, User to);
    void addToBlackList(User from, User to);
    void removeFromBlackList(User from, User to);
    List<User> getBlackListFriends(User user);
    void addPendingMessage(User from, User target, String type, String object);
    Map<String, List<String>> getPendingMessagesFor(User target);
    void deletePendingMessages(User user);
    void changeProfilePhoto(User user, Storage avatar);
    void deleteProfilePhoto(User user);
}
