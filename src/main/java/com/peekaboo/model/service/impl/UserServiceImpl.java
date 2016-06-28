package com.peekaboo.model.service.impl;

import com.peekaboo.model.dao.GenericDao;
import com.peekaboo.model.entity.User;
import com.peekaboo.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

@Service(value = "userService")
public class UserServiceImpl implements UserService {

    @Autowired
    @Qualifier("userDao")
    private GenericDao<User, Long> genericDao;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

       return null;
    }

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
