package com.yash.employeemanagement.controller;

import com.yash.employeemanagement.dto.ApiResponse;
import com.yash.employeemanagement.entity.User;
import com.yash.employeemanagement.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@RequestBody User user) {
        User registeredUser = authService.register(user);
        return ResponseEntity.ok(new ApiResponse<>(true, "User registered successfully", registeredUser));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody User user) {
        String token = authService.authenticate(user.getUsername(), user.getPassword());
        return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", token));
    }

}
