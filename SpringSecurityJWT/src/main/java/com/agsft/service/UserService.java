package com.agsft.service;

import com.agsft.entity.User;
import com.agsft.exception.UserNotFoundException;

import java.util.List;

public interface UserService {
    User registerUser(User user);
    User findUserById(int id) throws UserNotFoundException;

    User findUserByUsername(String username) throws UserNotFoundException;

    List<User> findAllUsers();

}
