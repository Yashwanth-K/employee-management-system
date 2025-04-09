package com.yash.employeemanagement.service;

import com.yash.employeemanagement.entity.User;
import com.yash.employeemanagement.repository.UserRepository;
import com.yash.employeemanagement.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    //Used to authenticate the user using Spring Security.
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username or password");
        }

        // If authentication passes, generate token
        return jwtUtil.generateToken(username);
    }

    public User register(User user) {
        //Hashes the password using PasswordEncoder
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if(user.getRole()==null|| user.getRole().isEmpty()){
            user.setRole("ROLE_USER"); // Default role
        }
//        Saves user to DB via UserRepository
        return userRepository.save(user);
    }
}
