package com.example.sb_auth_system.controller;

import com.example.sb_auth_system.entity.Users;
import com.example.sb_auth_system.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class AuthController {

    @Autowired
    AuthService authService;

    @GetMapping("/tet")
    public String tet() {
        return "WORKING";
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Users user){
        return  ResponseEntity.ok(authService.login(user));
    }

    @PostMapping("/register")
    public ResponseEntity<Users> register(@RequestBody Users user){
        System.out.println("regiter");
        return  ResponseEntity.ok(authService.register(user));
    }

}
