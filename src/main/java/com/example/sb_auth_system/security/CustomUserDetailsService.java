package com.example.sb_auth_system.security;

import com.example.sb_auth_system.entity.Users;
import com.example.sb_auth_system.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    public CustomUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users userDetail = userRepo.findByEmail(email);
        if (userDetail != null){
            CustomUserDetails customUserDetails ;
        }
        return null;
    }
}
