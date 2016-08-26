package com.peekaboo.model.service.impl;

import com.peekaboo.model.entity.User;
import com.peekaboo.model.entity.VerificationToken;
import com.peekaboo.model.repository.VerificationTokenRepository;
import com.peekaboo.model.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;

@Service(value = "verificationTokenService")
public class VerificationTokenServiceImpl implements VerificationTokenService {

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Override
    public VerificationToken create(VerificationToken entity) {
        return null;
    }

    @Override
    public VerificationToken get(String id) {
        return null;
    }

    @Override
    public void update(VerificationToken entity) {

    }

    @Override
    public void delete(VerificationToken entity) {

    }

    @Override
    public VerificationToken findByValue(String token) {
        return null;
    }

    @Override
    public String deleteByValue(String value) {
        return null;
    }

    @Override
    public VerificationToken findByUser(User user) {
        return null;
    }
//
//    @Autowired
////    private VerificationTokenRepository verificationTokenRepository;
//
//    @Override
//    public VerificationToken create(VerificationToken entity) {
//        return verificationTokenRepository.saveAndFlush(entity);
//    }
//
//    @Override
//    public VerificationToken get(String id) {
//        return verificationTokenRepository.findOne(id);
//    }
//
//    @Override
//    public void update(VerificationToken entity) {
//        verificationTokenRepository.saveAndFlush(entity);
//    }
//
//    @Override
//    public void delete(VerificationToken entity) {
//        verificationTokenRepository.delete(entity);
//    }
//
//    @Override
//    public VerificationToken findByValue(String value) {
//        return verificationTokenRepository.findByValue(value);
//    }
//
//    @Override
//    @Transactional
//    public String deleteByValue(String value) {
//        return verificationTokenRepository.deleteByValue(value);
//    }
//
//    @Override
//    public VerificationToken findByUser(User user){
//        return verificationTokenRepository.findByUser(user);
//    }
}
