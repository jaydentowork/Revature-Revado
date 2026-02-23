package com.revature.revado.services;

import com.revature.revado.exceptions.ResourceNotFoundException;
import com.revature.revado.models.User;
import com.revature.revado.repositories.UserRepository;
import com.revature.revado.security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.revature.revado.constants.ErrorMessages.LOGIN_FAILED;
import static com.revature.revado.constants.ErrorMessages.TAKEN_USERNAME;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    //1. Check if username is taken
    //2. Hash password
    //3. Save user to database
    public User register(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException(TAKEN_USERNAME);
        }
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    //1. Authenticate using the authentication manager
    //2. Generate and return the jwt token
    public String login(String username, String password) {
        // 1. Hand the credentials to the Manager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        // 2. Set the Security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException(LOGIN_FAILED));
        return jwtUtils.generateJwtToken(user.getId(), user.getUsername());
    }
}
