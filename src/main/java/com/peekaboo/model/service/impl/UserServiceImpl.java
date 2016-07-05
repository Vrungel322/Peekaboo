package com.peekaboo.model.service.impl;

import com.peekaboo.model.entity.User;
import com.peekaboo.model.repository.UserRepository;
import com.peekaboo.model.repository.VerificationTokenRepository;
import com.peekaboo.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Override
    @Transactional
    public User add(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public User get(Long id) {
        return userRepository.findOne(id);
    }

    @Override
    @Transactional
    public void update(User user) {
        userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public boolean emailExist(String email) {
        return userRepository.findByUsername(email) != null;
    }

    @Transactional
    public User findByToken(String token) {
        return verificationTokenRepository.findByToken(token).getUser();
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
