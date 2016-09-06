package com.peekaboo.model.service;


import com.peekaboo.model.entity.Storage;
import com.peekaboo.model.entity.User;

import java.util.List;


public interface StorageService {
    List<Storage> findByUser(User user);
    Storage findByFileName(String fileName);
    void save(Storage entity);
    void delete(Storage entity);
    void update(Storage entity);
}
