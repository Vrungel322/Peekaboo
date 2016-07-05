package com.peekaboo.model.service.impl;

import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.VerificationToken;
import com.peekaboo.model.repository.VerificationTokenRepository;
import com.peekaboo.model.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "verificationTokenService")
public class VerificationTokenServiceImpl implements VerificationTokenService {

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Override
    @Transactional
    public VerificationToken create(VerificationToken entity) {
        return verificationTokenRepository.saveAndFlush(entity);
    }

    @Override
    @Transactional
    public VerificationToken get(String id) {
        return verificationTokenRepository.findOne(id);
    }

    @Override
    @Transactional
    public void update(VerificationToken entity) {
        verificationTokenRepository.saveAndFlush(entity);
    }

    @Override
    @Transactional
    public void delete(VerificationToken entity) {
        verificationTokenRepository.delete(entity);
    }

    @Override
    @Transactional
    public VerificationToken findByValue(String value) {
        return verificationTokenRepository.findByValue(value);
    }

    @Override
    @Transactional
    public String deleteByValue(String value) {
        return verificationTokenRepository.deleteByValue(value);
    }

    @Override
    @Transactional
    public VerificationToken findByUser(User user){
        return verificationTokenRepository.findByUser(user);
    }
}
