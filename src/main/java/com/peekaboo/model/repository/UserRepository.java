package com.peekaboo.model.repository;

import com.peekaboo.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    User findByUsername(String username);

    User findByEmail(String email);

    User findByTelephone(String telephone);

    User findByEmailOrTelephone(String email, String telephone);
}
