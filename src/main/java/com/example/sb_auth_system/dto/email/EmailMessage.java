package com.example.sb_auth_system.dto.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessage {

    private String email;
    private String username;
    private EmailType type;
    private String data; // code or token
}