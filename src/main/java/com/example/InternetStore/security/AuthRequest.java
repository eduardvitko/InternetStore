package com.example.InternetStore.security;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;

    public Object getUsername() {
        return username;
    }

    public Object getPassword() {
        return password;
    }
}
