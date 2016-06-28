package com.peekaboo.model.service;

import com.peekaboo.model.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends EntityService<User, Long>, UserDetailsService {

    User findByUsername(String username);
}
