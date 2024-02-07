package com.agsft.service;

import com.agsft.entity.User;
import com.agsft.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JWTUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        System.out.println(username);
        User user = userRepository.findByUsername(username);

        if(user != null){

//            System.out.println("Username : "+user.getUsername()+ " " + user.getRole());

            List<GrantedAuthority> authorities = new ArrayList<>();

            SimpleGrantedAuthority sga = new SimpleGrantedAuthority(user.getRole());
            authorities.add(sga);

            return new org.springframework.security.core.userdetails.User(username, user.getPassword(),authorities);
        }
        else{
            throw new BadCredentialsException("User Details not found with this username: " + username);
        }
    }
}
