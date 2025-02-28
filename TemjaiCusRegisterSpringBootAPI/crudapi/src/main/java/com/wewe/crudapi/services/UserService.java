package com.wewe.crudapi.services;

import com.wewe.crudapi.entity.User;

import java.util.List;

public interface UserService {

    User save(User user);

    List<User> findAll();

    User findById(Integer id);

    void deleteById(Integer id);
}
