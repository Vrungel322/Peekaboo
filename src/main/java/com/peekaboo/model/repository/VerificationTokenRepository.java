package com.peekaboo.model.repository;

import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.VerificationToken;
import org.springframework.stereotype.Repository;

public interface VerificationTokenRepository  {

    void save(VerificationToken entity);
    VerificationToken create(VerificationToken token);
    void delete(VerificationToken entity);
    void update(VerificationToken verificationToken);
    VerificationToken findById(Long id);
    VerificationToken findByValue(String value);
    void deleteByValue(String value);
    VerificationToken findByUser(User user);

}