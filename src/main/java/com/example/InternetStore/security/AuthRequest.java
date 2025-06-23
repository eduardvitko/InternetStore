package com.example.InternetStore.security;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
    private String phone;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    public String getPhone() {
        return phone;
    }
}
