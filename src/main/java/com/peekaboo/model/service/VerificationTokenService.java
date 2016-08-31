package com.peekaboo.model.service;

import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.VerificationToken;

public interface VerificationTokenService extends EntityService<VerificationToken, String> {

    VerificationToken findByValue(String token);
    void deleteByValue(String value);
    VerificationToken findByUser(User user);
    void save(VerificationToken entity);
    void delete(VerificationToken entity);
    void update(VerificationToken verificationToken);
}
