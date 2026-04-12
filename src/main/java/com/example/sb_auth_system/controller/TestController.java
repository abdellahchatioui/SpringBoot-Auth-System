package com.example.sb_auth_system.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    // @PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('DELETE_USER')")
    @GetMapping("/admin")
    public String adminPage() {
        return "ADMIN PAGE";
    }

    // @PreAuthorize("hasRole('USER')")
    @PreAuthorize("hasAuthority('READ_USER')")
    @GetMapping("/user")
    public String userPage() {
        return "USER PAGE";
    }

    @GetMapping("/test")
    public String test() {
        return "WORKING";
    }

}
