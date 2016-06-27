package com.peekaboo.model.service;

import java.io.Serializable;

public interface EntityService<T, PK extends Serializable> {

    void add(T entity);

    T get(PK id);

    void update(T entity);

    void delete(T entity);
}
