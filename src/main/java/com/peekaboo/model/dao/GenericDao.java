package com.peekaboo.model.dao;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface GenericDao<T, PK extends Serializable> {

    void create(T obj);

    T read(PK key);

    void update(T obj);

    void delete(T obj);

    CriteriaBuilder getBuilder();

    List<T> findAll();
}
