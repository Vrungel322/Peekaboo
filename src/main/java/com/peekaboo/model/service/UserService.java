package com.peekaboo.model.service;

import com.peekaboo.model.entity.User;

public interface UserService extends EntityService<User, String> {

    User findByUsername(String username);

    boolean loginExists(String login);

//    User findByConfirmToken(String token);

//    User findByEmail(String email);
//
//    User findByTelephone(String telephone);

    User findByLogin(String login);

//    boolean userExist(String login);
}
