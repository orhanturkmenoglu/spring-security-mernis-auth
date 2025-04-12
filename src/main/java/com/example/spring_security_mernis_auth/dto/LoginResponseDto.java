package com.example.spring_security_mernis_auth.dto;

public class LoginResponseDto {
    private String message;

    public LoginResponseDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
