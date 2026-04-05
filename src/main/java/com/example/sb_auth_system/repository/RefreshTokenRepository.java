package com.example.sb_auth_system.repository;

import com.example.sb_auth_system.entity.RefreshToken;
import com.example.sb_auth_system.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findAllByUser(Users user);

    void deleteByToken(String token);

    @Modifying
    void deleteByUser(Users user);
}
