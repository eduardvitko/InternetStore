package com.example.InternetStore.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data

public class AuthResponse {
    private String token;

    public AuthResponse(String token) {
        this.token = token;  // вот это надо добавить!
    }
}
