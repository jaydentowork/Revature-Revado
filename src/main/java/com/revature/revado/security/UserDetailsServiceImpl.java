package com.revature.revado.security;

import com.revature.revado.repositories.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

//This class is for user look up, once a user send there username/password, we will bind them to a User of spring security.
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username){
        com.revature.revado.models.User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Username is not found."));
        return User.withUsername(user.getUsername()).password(user.getPassword()).authorities("USER").build();
    }

}
