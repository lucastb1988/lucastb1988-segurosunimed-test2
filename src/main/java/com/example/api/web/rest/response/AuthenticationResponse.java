package com.example.api.web.rest.response;

public class AuthenticationResponse {

    private String accessToken;

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}