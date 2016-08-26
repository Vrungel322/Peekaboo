package com.peekaboo.model.repository.impl;


import com.peekaboo.model.Neo4jSessionFactory;
import com.peekaboo.model.entity.Storage;
import com.peekaboo.model.entity.User;
import com.peekaboo.model.repository.StorageRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class StorageRepositoryImpl implements StorageRepository {

    @Autowired
    private Neo4jSessionFactory sessionFactory;

    public StorageRepositoryImpl(){

    }

    public StorageRepositoryImpl(Neo4jSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Storage entity) {
        sessionFactory.getSession().save(entity);
    }

    @Override
    public void update(Storage entity) {
        sessionFactory.getSession().save(entity);
    }

    @Override
    public void delete(Storage entity) {
        sessionFactory.getSession().delete(entity);
    }

    @Override
    public Storage findById(Long id) {
        return sessionFactory.getSession().load(Storage.class, id);
    }

    @Override
    public Storage findByUser(User user) {
        return user.getStorage();
    }

    @Override
    public void addUser(Storage storage, User user) {
        user.getStorages().add(storage);
    }
}
