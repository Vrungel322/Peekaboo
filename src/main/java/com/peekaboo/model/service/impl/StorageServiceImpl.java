package com.peekaboo.model.service.impl;


import com.peekaboo.model.entity.Storage;
import com.peekaboo.model.entity.User;
import com.peekaboo.model.repository.impl.StorageRepositoryImpl;
import com.peekaboo.model.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "storageServices")
public class StorageServiceImpl implements StorageService {

    @Autowired
    private StorageRepositoryImpl storageRepository;

    @Override
    public void save(Storage entity) {
        storageRepository.save(entity);
    }

    @Override
    public void delete(Storage entity) {
        storageRepository.delete(entity);
    }

    @Override
    public List<Storage> findByUser(User user) {
        return user.getOwnStorages();
    }

    @Override
    public Storage findByFileName(String fileName) {
        return storageRepository.findByFileName(fileName);
    }

    @Override
    public void update(Storage entity) {
        storageRepository.save(entity);
    }
}