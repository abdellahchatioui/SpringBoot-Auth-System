package com.example.sb_auth_system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class TokenBlacklist {

    @Id
    private Long id;

    @Column(nullable = false,unique = true)
    private String token;

}
