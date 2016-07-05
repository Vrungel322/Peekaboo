package com.peekaboo.model.service;

import com.peekaboo.model.entity.VerificationToken;

public interface VerificationTokenService extends EntityService<VerificationToken, Long> {

    VerificationToken findByToken(String token);
}
