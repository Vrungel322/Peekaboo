package com.peekaboo.repository;

import com.peekaboo.entity.User;

public interface UserRepository {

    void create(User user);
    User getByKey(Long id);


}
