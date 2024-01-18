package com.example.api.web.rest.request;

import javax.validation.constraints.NotNull;

public class AuthenticationRequest {

    @NotNull
    private String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}