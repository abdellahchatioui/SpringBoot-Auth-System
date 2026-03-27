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

    public String checkUser(Users user){
        Optional<Users> findUser = userRepos.findByEmail(user.getEmail());
        if (findUser.isPresent()){
            if(passwordEncoder.matches(user.getPassword(),findUser.get().getPassword())){
                return "TOKEN ....";
            }
        }
        return "User not found ! ";
    }

    public Users saveUser(Users user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.valueOf("USER"));
        userRepos.save(user);
        return user;
    }


}
