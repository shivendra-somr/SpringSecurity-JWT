package com.agsft.controller;

import com.agsft.entity.User;
import com.agsft.exception.UserNotFoundException;
import com.agsft.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/user/create")
    public ResponseEntity<User> registerNewUser(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return new ResponseEntity<>(userService.registerUser(user), HttpStatus.CREATED);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) throws UserNotFoundException {
        return new ResponseEntity<>(userService.findUserById(id), HttpStatus.OK);
    }

    @GetMapping("/user/allUsers")
    public ResponseEntity<List<User>> getAllUsers()  {
        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/signIn")
    public ResponseEntity<String> getLoggedInUserDetailsHandler(Authentication auth) throws UserNotFoundException {

        System.out.println(auth); // this Authentication object having Principle object details

        User user= userService.findUserByUsername(auth.getName());

        return new ResponseEntity<>(user.getUsername()+" Logged In Successfully", HttpStatus.ACCEPTED);


    }

}
