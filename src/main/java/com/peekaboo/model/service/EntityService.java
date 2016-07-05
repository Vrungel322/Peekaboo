package com.peekaboo.model.service;

import java.io.Serializable;

public interface EntityService<T, PK extends Serializable> {

    T create(T entity);

    T get(PK id);

    void update(T entity);

    void delete(T entity);
}
