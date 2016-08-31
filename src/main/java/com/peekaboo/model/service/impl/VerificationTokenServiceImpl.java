package com.peekaboo.model.service.impl;

import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.VerificationToken;
import com.peekaboo.model.repository.VerificationTokenRepository;
import com.peekaboo.model.repository.impl.VerificationRepositoryImpl;
import com.peekaboo.model.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;

@Service(value = "verificationTokenService")
public class VerificationTokenServiceImpl implements VerificationTokenService {

    @Autowired
    private VerificationRepositoryImpl verificationTokenRepository;

    @Override
    public VerificationToken create(VerificationToken entity) {
        verificationTokenRepository.save(entity);
        return entity;
    }
    @Override
    public VerificationToken get(String id) {
        return verificationTokenRepository.findById(Long.valueOf(id));
    }

    @Override
    public VerificationToken findByValue(String token) {
        return verificationTokenRepository.findByValue(token);
    }

    @Override
    public void deleteByValue(String value) {
        verificationTokenRepository.deleteByValue(value);
    }

    @Override
    public VerificationToken findByUser(User user) {
        return verificationTokenRepository.findByUser(user);
    }

    @Override
    public void save(VerificationToken entity) {
        verificationTokenRepository.save(entity);
    }

    @Override
    public void delete(VerificationToken entity) {
        verificationTokenRepository.delete(entity);
    }

    @Override
    public void update(VerificationToken verificationToken) {
        save(verificationToken);
    }

}
