package com.example.sb_auth_system.service;

import com.example.sb_auth_system.entity.RefreshToken;
import com.example.sb_auth_system.entity.Users;
import com.example.sb_auth_system.repository.RefreshTokenRepository;
import com.example.sb_auth_system.repository.UserRepository;
import com.example.sb_auth_system.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    JwtService  jwtService;

    public RefreshToken createRefreshToken(Users user){
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(user);
        refreshToken.setExpiryDate(
                Instant.now().plus(7, ChronoUnit.DAYS)
        );
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

    public RefreshToken verifyAndGetToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .map(this::verifyExpiration)
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }

    public String generateNewAccessToken(String refreshTokenString) {
        RefreshToken refreshToken = verifyAndGetToken(refreshTokenString);
        Users user = refreshToken.getUser();

        return jwtService.generateToken(user);
    }

}
