package com.sample.bookstore1.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;

    private long expiresIn;

    public String getToken() {
        return token;
    }

}