package com.peekaboo.model.service.impl;

import com.peekaboo.model.entity.Storage;
import com.peekaboo.model.entity.User;
import com.peekaboo.model.repository.impl.UserRepositoryImpl;
import com.peekaboo.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service(value = "userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepositoryImpl userRepository;


    public UserServiceImpl() {
    }

    @Override
    public User create(User entity) {
        userRepository.save(entity);
        return entity;
    }

    @Override
    public User get(String id) {
        return userRepository.findById(Long.valueOf(id));
    }

    @Override
    public void update(User entity) {
        userRepository.update(entity);
    }

    @Override
    public void delete(User entity) {
        userRepository.delete(entity);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByLogin(String login) {
        return userRepository.findByUsername(login);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByTelephone(String telephone) {
        return userRepository.findByTelephone(telephone);
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public void clearDataBase() {
        userRepository.clearDataBase();
    }

    @Override
    public List<User> getFriends(User user) {
        return userRepository.getFriends(user);
    }

    @Override
    public void addNewFriend(User target, User whom) {
        userRepository.addNewFriend(target, whom);
    }

    @Override
    public void deleteFriend(User from, User to) {
        userRepository.deleteFriend(from, to);
    }

    @Override
    public void addToBlackList(User from, User to) {
        userRepository.addToBlackList(from, to);
    }

    @Override
    public void removeFromBlackList(User from, User to) {
        userRepository.removeFromBlackList(from, to);
    }

    @Override
    public List<User> getBlackListFriends(User user) {
        return userRepository.getBlackListFriends(user);
    }

    @Override
    public void changeProfilePhoto(User user, Storage avatar) {
        userRepository.changeProfilePhoto(user, avatar);
    }

    @Override
    public void deleteProfilePhoto(User user) {
        userRepository.deleteProfilePhoto(user);
    }

    @Override
    public boolean emailExist(String email) {
        return userRepository.emailExist(email);
    }

    @Override
    public boolean phoneExist(String phone) {
        return userRepository.phoneExist(phone);
    }

    @Override
    public void addPendingMessage(User from, User target, String type, String object) {
        userRepository.addPendingMessage(from, target, type, object);
    }

    @Override
    public Map<String, List<String>> getPendingMessagesFor(User target) {
        return userRepository.getPendingMessagesFor(target);
    }

    @Override
    public void deletePendingMessages(User user) {
        userRepository.deletePendingMessages(user);
    }

    @Override
    public void sendFriendshipRequest(User user, User target) {
        userRepository.sendFriendshipRequest(user, target);
    }

    @Override
    public void deleteFriendshipRequest(User user, User target) {
        userRepository.deleteFriendshipRequest(user, target);
    }
}





