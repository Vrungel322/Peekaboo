package com.peekaboo.model.service;

import com.peekaboo.model.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends EntityService<User, String> {

    User findByLogin(String username);

    boolean loginExist(String login);

    User findByConfirmToken(String token);
}
