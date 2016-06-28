package com.peekaboo.model.dao.impl;

import com.peekaboo.model.dao.GenericDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

public class GenericDaoImpl<T, PK extends Serializable> implements GenericDao<T, PK> {

    @PersistenceContext
    private EntityManager entityManager;

    private final Class<T> type;

    public GenericDaoImpl(Class<T> type) {
        this.type = type;
    }

    @Override
    public void create(T obj) {
        entityManager.persist(obj);
    }

    @Override
    public T read(PK key) {
        return entityManager.find(type, key);
    }

    @Override
    public void update(T obj) {
        entityManager.merge(obj);
    }

    @Override
    public void delete(T obj) {
        entityManager.remove(obj);
    }

    @Override
    public CriteriaBuilder getBuilder() {
        return entityManager.getCriteriaBuilder();
    }

    @Override
    public List<T> findAll() {
        return entityManager.createQuery("SELECT a FROM " + type.getSimpleName() + " a").getResultList();
    }
}
