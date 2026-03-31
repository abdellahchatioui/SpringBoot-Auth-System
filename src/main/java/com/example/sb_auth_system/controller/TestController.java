package com.example.sb_auth_system.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/users")
    public String users(){
        return "SECURED DATA";
    }

    @GetMapping("/test")
    public String test() {
        return "WORKING";
    }

}
