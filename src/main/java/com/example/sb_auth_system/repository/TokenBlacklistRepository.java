package com.example.sb_auth_system.repository;

import com.example.sb_auth_system.entity.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist,Long> {

    Optional<TokenBlacklist> findByToken(String token);

    boolean existsByToken(String token);

}
