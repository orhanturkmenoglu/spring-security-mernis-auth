package com.example.spring_security_mernis_auth.dto;

public class LoginResponseDto {
    private String accessToken;

    public LoginResponseDto() {}

    public LoginResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "LoginResponseDto{" +
                "accessToken='" + accessToken + '\'' +
                '}';
    }
}
