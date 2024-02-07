package com.agsft.service;

import com.agsft.entity.User;
import com.agsft.exception.UserNotFoundException;
import com.agsft.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;
    @Override
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findUserById(int id) throws UserNotFoundException {
        if(userRepository.findById(id).isPresent()){
            return userRepository.findById(id).get();
        }
        throw new UserNotFoundException("User id is invalid");
    }

    @Override
    public User findUserByUsername(String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new UserNotFoundException("User name is not found");
        }
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
