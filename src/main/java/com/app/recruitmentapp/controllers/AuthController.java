package com.app.recruitmentapp.controllers;

import com.app.recruitmentapp.entities.Admin;
import com.app.recruitmentapp.entities.Candidate;
import com.app.recruitmentapp.entities.Recruiter;
import com.app.recruitmentapp.security.JwtUtil;
import com.app.recruitmentapp.services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        try {
            Map<String, Object> result = userService.login(email, password);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = jwtUtil.extractToken(request);
        if (token != null) {
            userService.logout(token);
            return ResponseEntity.ok(Map.of("message", "Déconnexion réussie").toString());
        }
        return ResponseEntity.badRequest().body("Token manquant");
    }

}

