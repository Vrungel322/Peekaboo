package com.peekaboo.model.repository;

import com.peekaboo.model.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {

    User findByUsername(String username);

    User findByEmail(String email);

    User findByTelephone(String telephone);

    User findByEmailOrTelephone(String email, String telephone);
}
