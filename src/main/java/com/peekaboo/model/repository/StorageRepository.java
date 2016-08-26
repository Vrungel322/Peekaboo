package com.peekaboo.model.repository;


import com.peekaboo.model.entity.Storage;
import com.peekaboo.model.entity.User;

public interface StorageRepository {
    void save(Storage entity);
    void delete(Storage entity);
    void update(Storage entity);
    void addUser(Storage storage, User user);
    Storage findById(Long id);
    Storage findByUser(User user);

}
