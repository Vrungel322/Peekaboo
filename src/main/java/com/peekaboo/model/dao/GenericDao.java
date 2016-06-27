package com.peekaboo.model.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<T, PK extends Serializable> {

    void create(T obj);

    T read(PK key);

    void update(T obj);

    void delete(T obj);

    List<T> findAll();
}
