package com.example.api.repository.security;

import com.example.api.web.rest.response.AuthenticationResponse;
import org.springframework.stereotype.Repository;

import static java.util.UUID.randomUUID;

@Repository
public class OAuthRestRepository {

    public AuthenticationResponse gerarAccessToken() {
        return new AuthenticationResponse(randomUUID().toString());
    }
}