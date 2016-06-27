package com.peekaboo.model.service.impl;

import com.peekaboo.model.dao.GenericDao;
import com.peekaboo.model.entity.User;
import com.peekaboo.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    @Qualifier("userDao")
    private GenericDao<User, Long> genericDao;

    @Override
    @Transactional
    public void add(User user) {
        genericDao.create(user);
    }

    @Override
    @Transactional
    public User get(Long id) {
        return genericDao.read(id);
    }

    @Override
    @Transactional
    public void update(User user) {
        genericDao.update(user);
    }

    @Override
    @Transactional
    public void delete(User user) {
        genericDao.delete(user);
    }

    public GenericDao<User, Long> getGenericDao() {
        return genericDao;
    }

    public void setGenericDao(GenericDao<User, Long> genericDao) {
        this.genericDao = genericDao;
    }
}
