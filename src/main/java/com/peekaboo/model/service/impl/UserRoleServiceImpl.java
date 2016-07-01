package com.peekaboo.model.service.impl;

import com.peekaboo.model.entity.UserRole;
import com.peekaboo.model.repository.UserRoleRepository;
import com.peekaboo.model.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    @Transactional
    public UserRole add(UserRole userRole) {
        return userRoleRepository.saveAndFlush(userRole);
    }

    @Override
    @Transactional
    public UserRole get(Long id) {
        return userRoleRepository.findOne(id);
    }

    @Override
    @Transactional
    public void update(UserRole userRole) {
        userRoleRepository.saveAndFlush(userRole);
    }

    @Override
    @Transactional
    public void delete(UserRole userRole) {
        userRoleRepository.delete(userRole);
    }

    public UserRoleRepository getUserRoleRepository() {
        return userRoleRepository;
    }

    public void setUserRoleRepository(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }
}
