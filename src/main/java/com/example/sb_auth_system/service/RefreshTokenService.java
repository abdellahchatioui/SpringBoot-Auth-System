package com.example.sb_auth_system.service;

import com.example.sb_auth_system.entity.RefreshToken;
import com.example.sb_auth_system.entity.Users;
import com.example.sb_auth_system.repository.RefreshTokenRepository;
import com.example.sb_auth_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private Long refreshTokenDurationMs = 604800000L;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public RefreshToken createRefreshToken(Users user){
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findById(user.getId()));
        refreshToken.setExpiryDate(Instant.now().plusSeconds(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
       if(token.getExpiryDate().isBefore(Instant.now())){
           refreshTokenRepository.deleteByToken(token.getToken());
           throw new RuntimeException("Refresh token was expired. Please make a new signin request");
       }
       return token;
    }
}
