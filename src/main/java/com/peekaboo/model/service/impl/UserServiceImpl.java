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

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    @Qualifier("userDao")
    private GenericDao<User, Long> genericDao;

//    CriteriaBuilder cb = em.getCriteriaBuilder();
//
//    CriteriaQuery<Country> q = cb.createQuery(Country.class);
//    Root<Country> c = q.from(Country.class);
//    ParameterExpression<Integer> p = cb.parameter(Integer.class);
//    q.select(c).where(cb.gt(c.get("population"), p));

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        CriteriaBuilder builder = genericDao.getBuilder();
        CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);
        ParameterExpression<String> username = builder.parameter(String.class, "username");
        criteriaQuery.select(userRoot);

        criteriaQuery.where(Builder)
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
