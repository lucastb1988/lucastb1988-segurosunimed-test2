package com.example.api.service.security;

import com.example.api.domain.security.SessionInfo;
import com.example.api.domain.security.User;
import com.example.api.exception.UnauthorizedException;
import com.example.api.repository.security.OAuthRestRepository;
import com.example.api.repository.security.SessionInfoRepository;
import com.example.api.repository.security.UserRepository;
import com.example.api.web.rest.response.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private SessionInfoRepository sessionInfoRepository;

    private UserRepository userRepository;

    private OAuthRestRepository oauthProvider;

    @Autowired
    public AuthenticationService(SessionInfoRepository sessionInfoRepository, UserRepository userRepository, OAuthRestRepository oauthProvider) {
        this.sessionInfoRepository = sessionInfoRepository;
        this.userRepository = userRepository;
        this.oauthProvider = oauthProvider;
    }

    public AuthenticationResponse login(String username) {
        return authenticate(username);
    }

    private AuthenticationResponse authenticate(final String username) throws UnauthorizedException {
        AuthenticationResponse authenticationResponse;
        SessionInfo sessionInfo;
        User user = null;

        if (!username.isEmpty()) {
            try {
                user = userRepository.findByEmail(username);
            } catch (Exception e) {
                throw new UnauthorizedException("Invalid credentials!");
            }
        }

        if (user == null) {
            throw new UnauthorizedException("Invalid credentials!");
        }

        authenticationResponse = gerarAccessToken();
        sessionInfo = new SessionInfo(authenticationResponse.getAccessToken());
        sessionInfo.setUserId(user.getId());
        sessionInfoRepository.save(sessionInfo);

        return authenticationResponse;
    }

    private AuthenticationResponse gerarAccessToken() {
        return oauthProvider.gerarAccessToken();
    }

}