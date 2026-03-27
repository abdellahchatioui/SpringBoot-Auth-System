package com.example.sb_auth_system.controller;

import com.example.sb_auth_system.entity.Users;
import com.example.sb_auth_system.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Users user){
        return  ResponseEntity.ok(authService.login(user));
    }

    @PostMapping("/register")
    public ResponseEntity<Users> register(@RequestBody Users user){
        return  ResponseEntity.ok(authService.register(user));
    }

}
