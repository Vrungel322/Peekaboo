package com.peekaboo.model.service.impl;

import com.peekaboo.model.dao.GenericDao;
import com.peekaboo.model.entity.UserRole;
import com.peekaboo.model.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    @Qualifier("userRoleDao")
    private GenericDao<UserRole, Long> genericDao;

    @Override
    @Transactional
    public void add(UserRole userRole) {
        genericDao.create(userRole);
    }

    @Override
    @Transactional
    public UserRole get(Long id) {
        return genericDao.read(id);
    }

    @Override
    @Transactional
    public void update(UserRole userRole) {
        genericDao.update(userRole);
    }

    @Override
    @Transactional
    public void delete(UserRole userRole) {
        genericDao.delete(userRole);
    }

    public GenericDao<UserRole, Long> getGenericDao() {
        return genericDao;
    }

    public void setGenericDao(GenericDao<UserRole, Long> genericDao) {
        this.genericDao = genericDao;
    }
}
