package com.peekaboo.model.service.impl;

import com.peekaboo.model.entity.User;
import com.peekaboo.model.repository.UserRepository;
import com.peekaboo.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User create(User entity) {
        return null;
    }

    @Override
    public User get(String id) {
        return null;
    }

    @Override
    public void update(User entity) {

    }

    @Override
    public void delete(User entity) {

    }

    @Override
    public User findByUsername(String username) {
        return null;
    }

    @Override
    public boolean loginExists(String login) {
        return false;
    }

    @Override
    public User findByLogin(String login) {
        return null;
    }

//    @Autowired
//    private UserRepository userRepository;
//
////    @Autowired
////    private VerificationTokenRepository verificationTokenRepository;
//
//    @Override
//    public User create(User user) {
//        return userRepository.saveAndFlush(user);
//    }
//
//    @Override
//    public User get(String id) {
//        return userRepository.findOne(id);
//    }
//
//    @Override
//    public void update(User user) {
//        userRepository.saveAndFlush(user);
//    }
//
//    @Override
//    public void delete(User user) {
//        userRepository.delete(user);
//    }
//
//    @Override
//    public User findByUsername(String username) {
//        return userRepository.findByUsername(username);
//    }
//
//    @Override
//    public boolean loginExists(String login) {
//        return userRepository.findByEmailOrTelephone(login, login) != null;
//    }

//    @Override
//    @Transactional
//    public User findByConfirmToken(String token) {
//        return verificationTokenRepository.findByValue(token).getUser();
//    }

//    @Override
//    @Transactional
//    public User findByTelephone(String telephone) {
//        return userRepository.findByTelephone(telephone);
//    }
//
//    @Override
//    @Transactional
//    public User findByEmail(String email) {
//        return userRepository.findByEmail(email);
//    }

//    @Override
//    @Transactional
//    public boolean userExist(String login) {
//        return userRepository.findByEmailOrTelephone(login, login) != null;
//    }

//    @Override
//    public User findByLogin(String login) {
//        return userRepository.findByEmailOrTelephone(login, login);
//    }
}
