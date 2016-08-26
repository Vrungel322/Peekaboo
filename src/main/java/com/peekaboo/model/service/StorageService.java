package com.peekaboo.model.service;


import com.peekaboo.model.entity.Storage;
import com.peekaboo.model.entity.User;

public interface StorageService {
    Storage findByUser(User user);
    void save(Storage entity);
    void delete(Storage entity);
    void update(Storage entity);
}
