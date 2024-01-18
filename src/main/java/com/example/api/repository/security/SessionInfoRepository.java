package com.example.api.repository.security;

import com.example.api.domain.security.SessionInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionInfoRepository extends JpaRepository<SessionInfo, String> {

    SessionInfo findByAccessToken(String accessToken);
}