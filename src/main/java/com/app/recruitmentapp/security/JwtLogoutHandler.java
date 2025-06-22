package com.app.recruitmentapp.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class JwtLogoutHandler implements LogoutHandler {

    private final JwtUtil jwtUtil;

    public JwtLogoutHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        String token = jwtUtil.extractToken(request);
        if (token != null) {
            String email = jwtUtil.extractEmail(token);
            System.out.println("Déconnexion de l'utilisateur : " + email);
            // Rien d'autre à faire ici car stateless
        }
    }
}

