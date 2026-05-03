package com.example.sb_auth_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.Map;

@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false,unique = true)
    private String email;

    @Size(message = "min 4 word" , min = 4)
    @Column(nullable = false)
    private  String password;

    @Column(nullable = true)
    private String username;

    @Column(nullable = false)
    private Boolean  isVerified = false;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Users(Integer id, String email, String password,String username, Boolean isVerified,Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.isVerified = isVerified;
        this.role = role;
    }

    public Users() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getVerified() {
        return isVerified != null ? isVerified : false; // Safe check
    }

    public void setVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }
}
