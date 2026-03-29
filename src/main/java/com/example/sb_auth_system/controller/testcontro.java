package com.example.sb_auth_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class testcontro {

    @GetMapping
    public String test(){
        return "test....";
    }
}
