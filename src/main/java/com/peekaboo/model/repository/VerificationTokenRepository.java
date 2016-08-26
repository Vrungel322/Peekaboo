package com.peekaboo.model.repository;

import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.VerificationToken;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository  {

    VerificationToken findByValue(String token);

    String deleteByValue(String value);

    VerificationToken findByUser(User user);
}
