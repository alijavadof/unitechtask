package com.uniTech.uniTechTask.controller;

import com.uniTech.uniTechTask.exception.IncorrectCredentialsException;
import com.uniTech.uniTechTask.dto.request.LoginRequest;
import com.uniTech.uniTechTask.dto.request.RegisterRequest;
import com.uniTech.uniTechTask.service.UserDetailServiceImpl;
import com.uniTech.uniTechTask.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private UserService userservice;

    public AuthController(UserService userservice) {
        this.userservice = userservice;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        userservice.register(request);
        return new ResponseEntity<>("Successfully registered!", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        boolean isAuthenticated = userservice.authenticate(request.getPin(), request.getPassword());
        if (!isAuthenticated) {
            throw new IncorrectCredentialsException("Username or password is incorrect");
        }
        return new ResponseEntity<>("Welcome: " + request.getPin(), HttpStatus.OK);
    }
}
