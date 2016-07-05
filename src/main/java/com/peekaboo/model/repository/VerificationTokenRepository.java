package com.peekaboo.model.repository;

import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, String> {

    VerificationToken findByValue(String token);

    String deleteByValue(String value);

    VerificationToken findByUser(User user);
}
