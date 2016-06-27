package com.peekaboo.repository;

import com.peekaboo.model.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class UserRepositoryImpl implements UserRepository{

    private EntityManagerFactory emf;

    public EntityManagerFactory getEmf() {
        return emf;
    }

    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void create(User user) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
    }

    @Override
    public User getByKey(Long id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        User result = em.find(User.class, id);
        em.getTransaction().commit();
        return result;
    }
}
