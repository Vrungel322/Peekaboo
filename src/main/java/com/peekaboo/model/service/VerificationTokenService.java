package com.peekaboo.model.service;

import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.VerificationToken;

public interface VerificationTokenService extends EntityService<VerificationToken, String> {

    VerificationToken findByValue(String token);

    String deleteByValue(String value);

    VerificationToken findByUser(User user);
}
