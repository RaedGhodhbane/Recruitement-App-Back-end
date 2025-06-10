package com.app.recruitmentapp.services;

import com.app.recruitmentapp.entities.Admin;
import com.app.recruitmentapp.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    User saveUser(User user);

    User updateUser(Long id, User user);

    void deleteUser(Long id);
}
