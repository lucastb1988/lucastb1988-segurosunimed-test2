package com.example.api.service.security;

import com.example.api.domain.security.ProfileEnum;
import com.example.api.domain.security.SessionInfo;
import com.example.api.domain.security.User;
import com.example.api.exception.UnauthorizedException;
import com.example.api.repository.security.SessionInfoRepository;
import com.example.api.repository.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Lazy
public class AuthorizationService {

    private UserRepository userRepository;

    private SessionInfoRepository sessionInfoRepository;

    @Autowired
    public AuthorizationService(UserRepository userRepository, SessionInfoRepository sessionInfoRepository) {
        this.userRepository = userRepository;
        this.sessionInfoRepository = sessionInfoRepository;
    }

    public User authorize(final String accessToken, ProfileEnum... profiles) {
        SessionInfo sessionInfo = sessionInfoRepository.findByAccessToken(accessToken);

        if (sessionInfo == null) {
            throw new UnauthorizedException("Incorrect Token informed!");
        }

        Optional<User> user = userRepository.findById(sessionInfo.getUserId());
        if (!user.isPresent()) {
            throw new UnauthorizedException("User not found!");
        }

        for (ProfileEnum p : profiles) {
            if (user.get().getProfiles().contains(p.getDescription())) {
                return user.get();
            } else {
                throw new UnauthorizedException("User Profile without permission to proceed with the operation!");
            }
        }

        return null;
    }
}