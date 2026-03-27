package com.example.sb_auth_system.service;

import com.example.sb_auth_system.entity.Role;
import com.example.sb_auth_system.entity.Users;
import com.example.sb_auth_system.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepos;

    public AuthService(PasswordEncoder passwordEncoder, UserRepository userRepos) {
        this.passwordEncoder = passwordEncoder;
        this.userRepos = userRepos;
    }



    public Users saveUser(Users user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.valueOf("USER"));
        userRepos.save(user);
        return user;
    }


}
