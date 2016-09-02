package com.peekaboo.model.service.impl;

import com.peekaboo.model.Neo4jSessionFactory;
import com.peekaboo.model.entity.User;
import com.peekaboo.model.repository.impl.UserRepositoryImpl;
import com.peekaboo.model.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public boolean loginExists(String login) {

        User user = findByEmail(login);
        if (user == null)
            return false;
        else
            return true;
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
    public ArrayList<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public void clearDataBase() {
        userRepository.clearDataBase();
    }

    @Override
    public ArrayList<User> getFriends(User user) {
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
    public ArrayList<User> getBlackListFriends(User user) {
        return userRepository.getBlackListFriends(user);
    }
}





