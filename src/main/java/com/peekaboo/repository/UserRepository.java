package com.peekaboo.repository;

import com.peekaboo.model.entity.User;

public interface UserRepository {

    void create(User user);

    User getByKey(Long id);


}
