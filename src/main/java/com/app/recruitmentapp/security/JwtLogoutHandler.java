package com.app.recruitmentapp.security;

import com.app.recruitmentapp.services.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class JwtLogoutHandler implements LogoutHandler {

    private final JwtUtil jwtUtil;
    private final TokenBlacklistService tokenBlacklistService;

    public JwtLogoutHandler(JwtUtil jwtUtil, TokenBlacklistService tokenBlacklistService) {
        this.jwtUtil = jwtUtil;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        String token = jwtUtil.extractToken(request);
        if (token != null) {
            String email = jwtUtil.extractEmail(token);
            System.out.println("Déconnexion de l'utilisateur : " + email);
            LocalDateTime expiration = jwtUtil.extractExpiration(token).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            tokenBlacklistService.blacklistToken(token, expiration);
        }
    }
}

